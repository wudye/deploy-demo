package com.mwu.geodistance.base;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.condition.EnabledIf;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@EnabledIf("isDockerAvailable")
@Testcontainers
public abstract class AbstractTestContainerConfiguration {

    private static final DockerImageName REDIS_IMAGE = DockerImageName.parse("redis:7.4.1");

    static final GenericContainer<?> REDIS_CONTAINER =
            new GenericContainer<>(REDIS_IMAGE).withExposedPorts(6379);

    /**
     * 检查 Docker 是否可用（支持本地和远程 Docker）
     * 
     * 支持以下方式配置远程 Docker：
     * 1. 环境变量: DOCKER_HOST=tcp://192.168.80.131:2375
     * 2. Java 系统属性: -Ddocker.host=tcp://192.168.80.131:2375
     */
    static boolean isDockerAvailable() {
        try {
            // 尝试连接到 Docker（Testcontainers 会自动使用配置的 DOCKER_HOST）
            DockerClientFactory.instance().client();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @BeforeAll
    static void beforeAll() {
        REDIS_CONTAINER.withReuse(true);
        REDIS_CONTAINER.start();
    }
}

