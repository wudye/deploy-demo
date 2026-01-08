# 测试指南

## 概述

本项目使用 Testcontainers 进行集成测试，需要 Docker 环境。如果您的环境没有安装 Docker，测试将自动跳过。

## 测试类型

### 1. 单元测试
不需要 Docker 环境，可以直接运行：
```bash
mvn test
```

### 2. 集成测试（需要 Docker）
使用 Testcontainers 运行 Redis 容器：
```bash
# 确保 Docker 已启动
docker --version

# 运行所有测试
mvn test

# 只运行特定测试
mvn test -Dtest=GeoLocationControllerTest
```

## Docker 环境配置

### 方式 1: 本地 Docker

#### Windows 用户
1. 安装 Docker Desktop for Windows
   - 下载: https://www.docker.com/products/docker-desktop/
   - 安装并启动 Docker Desktop

2. 验证 Docker 安装：
```bash
docker --version
```

3. 验证 Docker 运行状态：
```bash
docker info
```

#### Linux/Mac 用户
```bash
# Ubuntu/Debian
sudo apt-get install docker.io

# macOS
brew install --cask docker
```

### 方式 2: 远程 Docker（推荐）

如果您的 Docker 运行在远程 VM 上（例如 192.168.80.131），需要配置 DOCKER_HOST 环境变量。

#### 步骤 1: 在远程 VM 上启用 Docker 远程 API

在远程 VM (192.168.80.131) 上：

```bash
# 编辑 Docker 守护进程配置
sudo vi /etc/docker/daemon.json

# 添加以下内容（如果文件不存在则创建）
{
  "hosts": ["unix:///var/run/docker.sock", "tcp://0.0.0.0:2375"]
}

# 重启 Docker 服务
sudo systemctl restart docker

# 验证端口是否监听
sudo netstat -tulpn | grep 2375
```

#### 步骤 2: 在本地配置 DOCKER_HOST

##### Windows PowerShell

```powershell
# 临时设置（仅当前会话有效）
$env:DOCKER_HOST="tcp://192.168.80.131:2375"
mvn test

# 或者永久设置（需要管理员权限）
[System.Environment]::SetEnvironmentVariable('DOCKER_HOST', 'tcp://192.168.80.131:2375', 'User')
```

##### Windows CMD

```cmd
# 临时设置（仅当前会话有效）
set DOCKER_HOST=tcp://192.168.80.131:2375
mvn test

# 永久设置
setx DOCKER_HOST "tcp://192.168.80.131:2375"
```

##### Linux/Mac

```bash
# 临时设置（仅当前会话有效）
export DOCKER_HOST=tcp://192.168.80.131:2375
mvn test

# 永久设置（添加到 ~/.bashrc 或 ~/.zshrc）
echo 'export DOCKER_HOST=tcp://192.168.80.131:2375' >> ~/.bashrc
source ~/.bashrc
```

##### 使用 Maven 传递系统属性

```bash
# 在运行测试时传递系统属性
mvn test -Ddocker.host=tcp://192.168.80.131:2375
```

#### 步骤 3: 验证远程 Docker 连接

```bash
# 设置 DOCKER_HOST 后验证
docker ps

# 应该能看到远程 VM 上的容器列表
```

## 测试跳过机制

如果 Docker 不可用，集成测试会自动跳过：

```java
@EnabledIf("isDockerAvailable")
class GeoLocationControllerTest extends AbstractRestControllerTest {

    static boolean isDockerAvailable() {
        try {
            org.testcontainers.DockerClientFactory.instance().client();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
```

## 运行测试

### 方式 1: 运行所有单元测试（不包含集成测试）
```bash
mvn test -DskipITs
```

### 方式 2: 使用远程 Docker 运行所有测试
```powershell
# Windows PowerShell
$env:DOCKER_HOST="tcp://192.168.80.131:2375"
mvn test
```

```bash
# Linux/Mac
export DOCKER_HOST=tcp://192.168.80.131:2375
mvn test
```

### 方式 3: 只运行特定测试类
```bash
mvn test -Dtest=VehicleLocationToVehicleLocationResponseMapperTest
```

### 方式 4: 使用 Maven 系统属性指定远程 Docker
```bash
mvn test -Ddocker.host=tcp://192.168.80.131:2375
```

## 常见问题

### 问题 1: Could not find a valid Docker environment
**错误信息:**
```
java.lang.IllegalStateException: Could not find a valid Docker environment.
```

**解决方案:**
1. 确保 DOCKER_HOST 已设置：
   ```powershell
   # Windows
   echo $env:DOCKER_HOST
   ```

   ```bash
   # Linux/Mac
   echo $DOCKER_HOST
   ```

2. 验证远程 Docker 可访问：
   ```bash
   curl http://192.168.80.131:2375/version
   ```

3. 确保防火墙允许连接：
   ```bash
   # 在远程 VM 上
   sudo ufw allow 2375/tcp
   ```

### 问题 2: Connection refused
**错误信息:**
```
Failed to connect to /192.168.80.131:2375
```

**解决方案:**
1. 确保远程 VM 上的 Docker 守护进程已启用 TCP 监听
2. 检查防火墙规则
3. 验证网络连通性：
   ```bash
   ping 192.168.80.131
   telnet 192.168.80.131 2375
   ```

### 问题 3: 测试失败
**解决方案:**
1. 清理并重新编译：
   ```bash
   mvn clean compile test
   ```

2. 查看详细日志：
   ```bash
   mvn test -X
   ```

3. 查看测试报告：
   ```
   target/surefire-reports/
   ```

## 测试报告

测试报告位置：
```
target/surefire-reports/
├── *.txt           # 文本格式报告
└── *.xml           # XML 格式报告（CI/CD 使用）
```

查看报告：
```powershell
# Windows
type target\surefire-reports\*.txt
```

```bash
# Linux/Mac
cat target/surefire-reports/*.txt
```

## IntelliJ IDEA 配置

### 配置远程 Docker

1. 打开 `Run/Debug Configurations`
2. 选择 `JUnit` 配置
3. 在 `VM Options` 中添加：
   ```
   -Ddocker.host=tcp://192.168.80.131:2375
   ```

### 配置环境变量

1. 打开 `Run/Debug Configurations`
2. 选择 `JUnit` 配置
3. 在 `Environment Variables` 中添加：
   ```
   DOCKER_HOST=tcp://192.168.80.131:2375
   ```
