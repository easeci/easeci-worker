package io.easeci.worker.engine.docker;

import com.github.dockerjava.api.DockerClient;
import io.easeci.worker.engine.ContainerSystemException;
import io.easeci.worker.properties.DockerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class DockerImageHolder {
    private final DockerClient dockerClient;
    private final DockerProperties dockerProperties;
    private final DockerfileResourcesLoader dockerfileResourcesLoader;
    private final DockerBuildResultCallback dockerBuildResultCallback;

    public void initializeRequiredImage() throws ContainerSystemException {
        final String dockerfilePath = dockerProperties.getBaseImage().getResourcesPath();
        final String producedImageTag = dockerProperties.getBaseImage().getProducedTag();
        if (isDockerImageExists(producedImageTag)) {
            log.info("Docker image tagged as: {} just exists in docker server", producedImageTag);
            return;
        }
        File dockerfile = dockerfileResourcesLoader.loadDockerFileFromResources(dockerfilePath);
        dockerClient.buildImageCmd()
                .withDockerfile(dockerfile)
                .withTags(Set.of(producedImageTag)).exec(dockerBuildResultCallback);
        try {
            dockerfileResourcesLoader.removeTemporaryDockerfile();
        } catch (IOException e) {
            log.error("IOException occurred while trying to remove temporary dockerfile created before", e);
        }
    }

    private boolean isDockerImageExists(String imageTag) {
        return dockerClient.listImagesCmd()
                .exec()
                .stream()
                .anyMatch(image -> Arrays.asList(image.getRepoTags()).contains(imageTag));
    }
}
