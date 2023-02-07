package io.easeci.worker.engine;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
@Singleton
class DockerClientFactory {

    DockerClient factorizeDockerClient() {
        return constructClient();
    }

    private DockerClient constructClient() {
        DockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .build();
        log.info("Docker connection created with API Version: {}", dockerClientConfig.getApiVersion().getVersion());

        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(dockerClientConfig.getDockerHost())
                .sslConfig(dockerClientConfig.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

        DockerHttpClient.Response response = httpClient.execute(DockerHttpClient.Request.builder()
                .method(DockerHttpClient.Request.Method.GET).path("/version")
                .build());
        DockerClient dockerClient = DockerClientImpl.getInstance(dockerClientConfig, httpClient);
        return dockerClient;
    }
}
