package io.easeci.worker.connection.state;

public interface StateQuery {

    NodeProcessingState getProcessingState();

    NodeConnectionState getNodeConnectionState();
}
