package io.easeci.worker.engine.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import io.micronaut.http.HttpStatus;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

import static io.easeci.worker.engine.docker.DockerConnectionState.CONNECTED;
import static io.easeci.worker.engine.docker.DockerConnectionState.NOT_CONNECTED;

@Slf4j
@Singleton
public class DockerClientProvider implements DockerMonitor {

    public DockerClient defaultDockerClient() {
        DockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        log.info("Docker connection created with API Version: {}", dockerClientConfig.getApiVersion().getVersion());
        final DockerHttpClient httpClient = defaultDockerHttpClient(dockerClientConfig);
        return DockerClientImpl.getInstance(dockerClientConfig, httpClient);
    }

    public DockerHttpClient defaultDockerHttpClient(DockerClientConfig dockerClientConfig) {
        return new ApacheDockerHttpClient.Builder()
                .dockerHost(dockerClientConfig.getDockerHost())
                .sslConfig(dockerClientConfig.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();
    }

    @Override
    public DockerConnectionState checkDockerConnection(DockerHttpClient dockerHttpClient) {
        final DockerHttpClient.Response response = dockerHttpClient.execute(DockerHttpClient.Request.builder()
                .method(DockerHttpClient.Request.Method.GET).path("/version")
                .build());
        log.info("Check docker connection responded with status: {}", response.getStatusCode());
        return response.getStatusCode() == HttpStatus.OK.getCode() ? CONNECTED : NOT_CONNECTED;
    }

    @Override
    public DockerConnectionState checkDefaultDockerConnection() {
        final DockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        final DockerHttpClient dockerHttpClient = defaultDockerHttpClient(dockerClientConfig);
        return checkDockerConnection(dockerHttpClient);
    }
}
