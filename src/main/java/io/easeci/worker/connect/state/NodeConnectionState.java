package io.easeci.worker.connect.state;

public enum NodeConnectionState {
    REQUESTED,
    ESTABLISHED,
    CONNECTION_ERROR,
    TIMEOUT,
    UNAUTHORIZED,
    IDLE,
    BUSY
}
