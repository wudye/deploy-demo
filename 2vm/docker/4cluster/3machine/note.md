epeatable plan to run a 3-master/3-slave Redis Cluster on 3 VMs with anti-affinity (each master’s slave on a different VM) using Docker Compose. No cross-host Docker networking is required; we expose host ports so nodes talk over the 192.168.80.x network.

1) Topology (recommended)
VM1 (192.168.80.129): Master A (6379), Slave of Master B (6380)
VM2 (192.168.80.130): Master B (6379), Slave of Master C (6380)
VM3 (192.168.80.131): Master C (6379), Slave of Master A (6380)

sudo mkdir -p ./data/6379 ./data/6380
# Set correct ownership (Redis UID is usually 999)
sudo chown -R 999:999 ./data/6379 ./data/6380

5) Create the cluster (run from any VM)
Use host IPs and exposed ports. From VM1, for example:
redis-cli --cluster create \
  192.168.80.129:6379 \
  192.168.80.130:6379 \
  192.168.80.131:6379 \
  192.168.80.129:6380 \
  192.168.80.130:6380 \
  192.168.80.131:6380 \
  --cluster-replicas 1

  This assigns one slave per master automatically, keeping 1:1 replica mapping.
If you enabled auth, append -a 123456 to the command and ensure requirepass/masterauth are set in all configs.

6) Verify cluster status
redis-cli -c -h 192.168.80.129 -p 6379 cluster info
redis-cli -c -h 192.168.80.129 -p 6379 cluster nodes
Look for:

cluster_state:ok
6 nodes listed with 3 masters, 3 slaves.

7) Check slot distribution and replica mapping
redis-cli -c -h 192.168.80.129 -p 6379 cluster nodes
# Each master should have a slave on a different IP per the mapping.

8) Basic read/write test
redis-cli -c -h 192.168.80.129 -p 6379 SET k1 v1
redis-cli -c -h 192.168.80.130 -p 6379 GET k1
