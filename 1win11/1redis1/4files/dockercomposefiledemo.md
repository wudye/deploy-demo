can be split 4 file to deploy
# File: `docker-compose-redis1.yml`
version: "3.8"
services:
  redis:
    image: redis:latest
    container_name: aitok_redis1
    command: ["redis-server", "--save", "", "--appendonly", "no", "--requirepass", "123456789"]
    ports:
      - "6379:6379"
    networks:
      redis_net1:
        ipv4_address: 172.28.10.10
    volumes:
      - redis1_data:/data
    restart: unless-stopped

networks:
  redis_net1:
    driver: bridge
    ipam:
      config:
        - subnet: 172.28.10.0/24
          gateway: 172.28.10.1

volumes:
  redis1_data:

# File: `docker-compose-redis2.yml`
version: "3.8"
services:
  redis:
    image: redis:latest
    container_name: aitok_redis2
    command: ["redis-server", "--save", "", "--appendonly", "no", "--requirepass", "123456789"]
    ports:
      - "6380:6379"
    networks:
      redis_net2:
        ipv4_address: 172.28.20.10
    volumes:
      - redis2_data:/data
    restart: unless-stopped

networks:
  redis_net2:
    driver: bridge
    ipam:
      config:
        - subnet: 172.28.20.0/24
          gateway: 172.28.20.1

volumes:
  redis2_data:

# File: `docker-compose-redis3.yml`
version: "3.8"
services:
  redis:
    image: redis:latest
    container_name: aitok_redis3
    command: ["redis-server", "--save", "", "--appendonly", "no", "--requirepass", "123456789"]
    ports:
      - "6381:6379"
    networks:
      redis_net3:
        ipv4_address: 172.28.30.10
    volumes:
      - redis3_data:/data
    restart: unless-stopped

networks:
  redis_net3:
    driver: bridge
    ipam:
      config:
        - subnet: 172.28.30.0/24
          gateway: 172.28.30.1

volumes:
  redis3_data:

# File: `docker-compose-redis4.yml`
version: "3.8"
services:
  redis:
    image: redis:latest
    container_name: aitok_redis4
    command: ["redis-server", "--save", "", "--appendonly", "no", "--requirepass", "123456789"]
    ports:
      - "6382:6379"
    networks:
      redis_net4:
        ipv4_address: 172.28.40.10
    volumes:
      - redis4_data:/data
    restart: unless-stopped

networks:
  redis_net4:
    driver: bridge
    ipam:
      config:
        - subnet: 172.28.40.0/24
          gateway: 172.28.40.1

volumes:
  redis4_data:
