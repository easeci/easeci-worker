package io.easeci.worker.pipeline;

import io.easeci.worker.connect.state.NodeConnectionState;
import io.easeci.worker.connect.state.NodeProcessingState;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Introspected
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponse {
    private NodeConnectionState nodeConnectionState;
    private NodeProcessingState nodeProcessingState;
    private long pipelineReceivedTime;
    private ScheduleErrorCode scheduleErrorCode;
}
