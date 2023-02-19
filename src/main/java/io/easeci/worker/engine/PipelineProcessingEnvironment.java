package io.easeci.worker.engine;

import io.easeci.worker.pipeline.Urls;
import lombok.Value;

import java.nio.file.Path;
import java.util.UUID;

@Value
public class PipelineProcessingEnvironment {
    String processingEnvironmentName;
    Path mountPoint;
    UUID pipelineContextId;
    Urls urls;
}
