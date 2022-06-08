package io.easeci.worker.connect.state;

import jakarta.inject.Singleton;

@Singleton
public class ConnectionStateService {

    public ConnectionStateResponse checkConnectionState(ConnectionStateRequest connectionStateRequest) {
        // mock todo
        return ConnectionStateResponse.builder()
                                      .nodeConnectionState(NodeConnectionState.ESTABLISHED)
                                      .build();
    }
}
