


1. Sentinel 查看从节点
   redis-cli -p 5000 SENTINEL replicas mymaster


# 2. 主节点角色与复制信息（如有密码加 -a 123456）
redis-cli -h 172.28.10.10 -p 6379 ROLE
redis-cli -h 172.28.10.10 -p 6379 INFO replication