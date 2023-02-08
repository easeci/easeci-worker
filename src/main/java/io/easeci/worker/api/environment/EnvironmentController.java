package io.easeci.worker.api.environment;

import io.easeci.worker.engine.DockerMonitor;
import io.easeci.worker.engine.DockerPlatformRunner;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Controller("/api/v1/environment")
public class EnvironmentController {

    private DockerMonitor dockerMonitor;
    private DockerPlatformRunner dockerPlatformRunner;

    @Get
    void fetchDockerImage() {
//        DockerPlatformRunner dockerPlatformRunner = new DockerPlatformRunner();
//        dockerPlatformRunner.fetchBuildContainer();
    }

    @Get("/install")
    void runAndPrepareBuildContainer() {
        dockerPlatformRunner.runContainer(Path.of("/tmp/easeci-worker/fa8f011e-aedb-426c-a042-e0a8675cc468"), UUID.randomUUID());
    }

    @Get("/docker/info")
    DockerConnectionStateResponse checkDockerConnection() {
        return new DockerConnectionStateResponse(dockerMonitor.checkDefaultDockerConnection());
    }
}
