package io.easeci.worker.properties;

import io.micronaut.context.annotation.ConfigurationProperties;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@Valid
@ToString
@ConfigurationProperties("docker")
public class DockerProperties {

    @NotNull
    private BaseImage baseImage;

    @Data
    @ConfigurationProperties("base-image")
    public static class BaseImage {
        @NotNull
        private String resourcesPath;

        @NotNull
        private String producedTag;

        @NotNull
        private String pipelineProcessingImage;
    }
}
