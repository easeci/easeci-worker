package io.easeci.worker.pipeline;

import io.easeci.worker.connect.state.NodeConnectionState;
import io.easeci.worker.connect.state.NodeProcessingState;
import jakarta.inject.Singleton;

import java.time.Instant;

@Singleton
public class PipelineExecutionFacade {

    public ScheduleResponse mockResponse() {
        return ScheduleResponse.builder()
                .nodeConnectionState(NodeConnectionState.ESTABLISHED)
                .nodeProcessingState(NodeProcessingState.BUSY)
                .pipelineReceivedTime(Instant.now().toEpochMilli())
                .scheduleErrorCode(null)
                .build();
    }
}
