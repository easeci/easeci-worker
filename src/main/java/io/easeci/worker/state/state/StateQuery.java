package io.easeci.worker.state.state;

public interface StateQuery {

    NodeProcessingState getProcessingState();

    NodeConnectionState getNodeConnectionState();
}
