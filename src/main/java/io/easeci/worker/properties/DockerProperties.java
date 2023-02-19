package io.easeci.worker.properties;

import io.easeci.worker.engine.docker.Image;
import io.micronaut.context.annotation.ConfigurationProperties;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Valid
@ToString
@ConfigurationProperties("docker")
public class DockerProperties {

    @NotNull
    private BaseImage baseImage;

    @NotNull
    private List<Image> predefinedImages;

    @Data
    @ConfigurationProperties("base-image")
    public static class BaseImage {
        @NotNull
        private String resourcesPath;

        @NotNull
        private String producedTag;
    }
}
