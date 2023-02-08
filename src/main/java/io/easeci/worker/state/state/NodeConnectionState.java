package io.easeci.worker.state.state;

public enum NodeConnectionState {
    REQUESTED,
    ESTABLISHED,
    CONNECTION_ERROR,
    TIMEOUT,
    UNAUTHORIZED,
    UNKNOWN
}
