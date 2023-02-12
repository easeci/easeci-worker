package io.easeci.worker.api.environment;

import io.easeci.worker.engine.docker.DockerMonitor;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Controller("/api/v1/environment")
public class EnvironmentController {

    private DockerMonitor dockerMonitor;

    @Get("/docker/info")
    DockerConnectionStateResponse checkDockerConnection() {
        return new DockerConnectionStateResponse(dockerMonitor.checkDefaultDockerConnection());
    }
}
