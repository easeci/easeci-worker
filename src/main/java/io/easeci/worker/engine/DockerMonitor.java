package io.easeci.worker.engine;

import com.github.dockerjava.transport.DockerHttpClient;

public interface DockerMonitor {

    DockerConnectionState checkDockerConnection(DockerHttpClient dockerHttpClient);

    DockerConnectionState checkDefaultDockerConnection();
}
