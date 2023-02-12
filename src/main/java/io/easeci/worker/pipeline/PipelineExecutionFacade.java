package io.easeci.worker.pipeline;

import io.easeci.worker.state.state.ConnectionStateService;
import io.easeci.worker.state.state.NodeConnectionState;
import io.easeci.worker.state.state.NodeProcessingState;
import io.easeci.worker.engine.docker.DockerPlatformRunner;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class PipelineExecutionFacade {
    private final ConnectionStateService connectionStateService;
    private final DockerPlatformRunner dockerPlatformRunner;

    public ScheduleResponse mockResponse() {
        return ScheduleResponse.builder()
                .nodeConnectionState(NodeConnectionState.ESTABLISHED)
                .nodeProcessingState(NodeProcessingState.BUSY)
                .pipelineReceivedTime(Instant.now().toEpochMilli())
                .scheduleErrorCode(null)
                .isSuccessfullyScheduled(true)
                .build();
    }

    public ScheduleResponse handlePipeline(ScheduleRequest scheduleRequest) throws IOException {
        log.info("Pipeline processing with pipelineContextId: {} just started", scheduleRequest.getPipelineContextId());

        byte[] scriptDecoded = Base64.getDecoder().decode(scheduleRequest.getScriptEncoded());

        Path pipelineContextDir = Files.createDirectories(Paths.get("/tmp/easeci-worker/".concat(scheduleRequest.getPipelineContextId().toString())));

        if (Files.exists(Path.of(pipelineContextDir.toString().concat("/pipeline-script.py")))) {
            Files.delete(Path.of(pipelineContextDir.toString().concat("/pipeline-script.py")));
        }
        Path file = Files.createFile(Path.of(pipelineContextDir.toString().concat("/pipeline-script.py")));
        Path executableFile = Files.write(file, scriptDecoded);




        dockerPlatformRunner.runContainer(pipelineContextDir, scheduleRequest.getPipelineContextId(), scheduleRequest.getMetadata().getUrls());
        return mockResponse();
    }
}
