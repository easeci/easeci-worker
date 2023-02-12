package io.easeci.worker.engine;

import io.easeci.worker.pipeline.Urls;

import java.nio.file.Path;
import java.util.UUID;

public interface Runner {

    void runContainer(Path mountPoint, UUID pipelineContextId, Urls urls);
}
