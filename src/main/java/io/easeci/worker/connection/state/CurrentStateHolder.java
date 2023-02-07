package io.easeci.worker.connection.state;

import io.easeci.commons.observer.Subscriber;
import io.easeci.worker.engine.PipelineStateEvent;
import io.easeci.worker.pipeline.PipelineState;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class CurrentStateHolder implements StateQuery, Subscriber<PipelineStateEvent> {
    private NodeConnectionState nodeConnectionState = NodeConnectionState.UNKNOWN;
    private NodeProcessingState nodeProcessingState = NodeProcessingState.IDLE;

    NodeProcessingState idle() {
        log.info("Worker Node state has been just changed to IDLE");
        this.nodeProcessingState = NodeProcessingState.IDLE;
        return this.nodeProcessingState;
    }

    NodeProcessingState busy() {
        log.info("Worker Node state has been just changed to BUSY");
        this.nodeProcessingState = NodeProcessingState.BUSY;
        return this.nodeProcessingState;
    }

    NodeConnectionState established() {
        this.nodeConnectionState = NodeConnectionState.ESTABLISHED;
        return this.nodeConnectionState;
    }

    @Override
    public NodeProcessingState getProcessingState() {
        return nodeProcessingState;
    }

    @Override
    public NodeConnectionState getNodeConnectionState() {
        return nodeConnectionState;
    }

    @Override
    public void update(PipelineStateEvent pipelineStateEvent) {
        if (PipelineState.FINISHED.equals(pipelineStateEvent.pipelineState())) {
            this.idle();
        } else if (PipelineState.RUN.equals(pipelineStateEvent.pipelineState())) {
            this.busy();
        }
    }
}
