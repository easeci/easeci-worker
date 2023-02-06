package io.easeci.worker.connection.state;

import jakarta.inject.Singleton;

@Singleton
class CurrentStateHolder implements StateQuery {
    private NodeConnectionState nodeConnectionState = NodeConnectionState.UNKNOWN;
    private NodeProcessingState nodeProcessingState = NodeProcessingState.IDLE;

    NodeProcessingState idle() {
        this.nodeProcessingState = NodeProcessingState.IDLE;
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
}
