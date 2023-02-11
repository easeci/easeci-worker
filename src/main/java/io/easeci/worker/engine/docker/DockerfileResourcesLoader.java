package io.easeci.worker.engine.docker;

import io.easeci.worker.engine.ContainerSystemException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
class DockerfileResourcesLoader {

    private File dockerfile;

    File loadDockerFileFromResources(String dockerfileResourcePath) throws ContainerSystemException {
        if (isNull(dockerfileResourcePath)) {
            throw new ContainerSystemException("Cannot load file because there is no property value of docker.base-image.resources-path");
        }
        Path tmpDockerFilePath = Path.of("/tmp/".concat(dockerfileResourcePath));
        InputStream dockerFileInputStream = getClass().getClassLoader().getResourceAsStream(dockerfileResourcePath);
        try {
            if (isNull(dockerFileInputStream)) {
                throw new ContainerSystemException(String.format("Cannot load file from resources path: %s InputStream was not able to read", dockerfileResourcePath));
            }
            else if (Files.notExists(tmpDockerFilePath.getParent())) {
                Files.createDirectories(tmpDockerFilePath.getParent());
            }
            else if (Files.notExists(tmpDockerFilePath)) {
                Path fileCreated = Files.createFile(tmpDockerFilePath);
                dockerfile = fileCreated.toFile();
            } else {
                dockerfile = tmpDockerFilePath.toFile();
            }
            FileUtils.copyInputStreamToFile(dockerFileInputStream, tmpDockerFilePath.toFile());
        } catch (IOException e) {
            throw new ContainerSystemException(String.format("Cannot load file from resources path: %s or cannot created file in temporary location: %s", dockerfileResourcePath, tmpDockerFilePath));
        }
        if (isNull(dockerfile) || Files.notExists(dockerfile.toPath())) {
            throw new ContainerSystemException("Dockerfile in temporary location still not exists but should");
        }
        return dockerfile;
    }

    void removeTemporaryDockerfile() throws IOException {
        Path temporaryDockerFile = dockerfile.toPath();
        if (Files.exists(dockerfile.toPath())) {
            Files.delete(temporaryDockerFile);
        }
    }
}
