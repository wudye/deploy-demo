# install ssh-server
    sudo apt update
    sudo apt install -y openssh-server
    sudo systemctl enable --now ssh
# change hostname
    使用 systemd（立即生效且持久）
    sudo hostnamectl set-hostname my-debian
    更新 /etc/hosts，避免本机服务因旧主机名解析出问题
    192.168.80.129 redis1
    192.168.80.130 redis2
    192.168.80.131 redis3
    192.168.80.132 redis4
# 配置 4 台 redis 为 ssh 免密码互相通信 with local pc
    win11 find the ssh key in local ps 
    Get-ChildItem $env:USERPROFILE\.ssh
    在 Windows PowerShell（或 Git Bash）上生成密钥（推荐 ed25519，或用 -t rsa）
    ssh-keygen -t ed25519 -C "your@host"        # 回车接受默认路径（%USERPROFILE%/.ssh/id_ed25519）

    推荐：用 ssh-copy-id（如果可用）把公钥复制到 VM（首次会要求密码）
    ssh-copy-id -i ~/.ssh/id_ed25519.pub mwu@192.168.80.132

    如果没有 ssh-copy-id（在 PowerShell 可用下面一行），把本地公钥追加到远端 authorized_keys
    Get-Content $env:USERPROFILE\.ssh\id_ed25519.pub | ssh mwu@192.168.80.132 "mkdir -p ~/.ssh && cat >> ~/.ssh/authorized_keys && chmod 700 ~/.ssh && chmod 600 ~/.ssh/authorized_keys"

    或者用 scp 上传再在远端追加
    scp ~/.ssh/id_ed25519.pub mwu@192.168.80.132:/tmp/id.pub
    ssh mwu@192.168.80.132 "mkdir -p ~/.ssh && cat /tmp/id.pub >> ~/.ssh/authorized_keys && rm /tmp/id.pub && chmod 700 ~/.ssh && chmod 600 ~/.ssh/authorized_keys"

    在 VM 上确认权限（可在本地通过 ssh 执行）
    ssh mwu@192.168.80.132 "ls -ld ~/.ssh; ls -l ~/.ssh/authorized_keys; chmod 700 ~/.ssh; chmod 600 ~/.ssh/authorized_keys"

    # 测试免密登录
    ssh mwu@192.168.80.132


# 需要启用内核的 vm.overcommit_memory=1。
    因为 Redis 在做 RDB 快照、AOF 重写或主从复制时会用 fork() 产生后台子进程。fork 后子进程会触发大量的 copy-on-write 内存分配检查：如果内核不允许内存过度分配（vm.overcommit_memory 为 0 或 2），内核可能在 fork/分配时拒绝，导致后台保存/复制失败、报错或进程被重启。设置 vm.overcommit_memory=1 允许内核过度分配，从而避免这类 fork 导致的失败。

    下面先说明要做的事，再给出一组命令（立即生效并永久写入 sysctl），最后重启 Redis 并检查日志。
    说明：先查看当前值；用 sysctl -w 立即生效；把设置写入 \/etc/sysctl.conf`` 以永久生效；重启 Redis 并检查日志确认警告消失
    # 查看当前值
    sudo sysctl vm.overcommit_memory

    # 立即设置（临时生效）
    sudo sysctl -w vm.overcommit_memory=1

    # 永久写入 /etc/sysctl.conf（先删除已有同名行再追加）
    sudo sed -i '/^vm\.overcommit_memory=/d' /etc/sysctl.conf
    echo 'vm.overcommit_memory=1' | sudo tee -a /etc/sysctl.conf

    # 重新加载 sysctl 配置
    sudo sysctl -p

    # 重启 Redis 服务（Debian/Ubuntu 常见服务名）
    sudo systemctl restart redis-server
    # 或（若服务名为 redis）
    sudo systemctl restart redis || true

    # 检查 Redis 日志 / 服务状态，确认 no WARNING
    sudo systemctl status redis-server --no-pager
    sudo journalctl -u redis-server -n 200 | grep -i overcommit || true
    # 或查看 Redis 日志文件（路径可能因发行版不同）
    sudo tail -n 100 /var/log/redis/redis-server.log || true


容器环境或由其他进程管理器（比如 docker、kubernetes）时，必须使用 daemonize no