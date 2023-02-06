package io.easeci.worker.connection.state;

public enum NodeConnectionState {
    REQUESTED,
    ESTABLISHED,
    CONNECTION_ERROR,
    TIMEOUT,
    UNAUTHORIZED,
    UNKNOWN
}
