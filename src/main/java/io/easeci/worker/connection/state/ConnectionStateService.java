package io.easeci.worker.connection.state;

import io.easeci.worker.connection.EaseCIWorkerProperties;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class ConnectionStateService {
    private final EaseCIWorkerProperties easeCIWorkerProperties;
    private final StateQuery stateQuery;
    private final CurrentStateHolder currentStateHolder;

    public ConnectionStateResponse checkConnectionState(ConnectionStateRequest connectionStateRequest) {
        if (NodeConnectionState.ESTABLISHED.equals(stateQuery.getNodeConnectionState())) {
            log.info("Checking state from Master Node: IP: {}, DOMAIN: {}",
                    connectionStateRequest.getNodeIp(), connectionStateRequest.getDomainName());
        } else {
            NodeConnectionState nodeConnectionState = currentStateHolder.established();
            log.info("Making connection with master node with: IP: {}, DOMAIN: {}, NodeConnectionState: {}",
                    connectionStateRequest.getNodeIp(), connectionStateRequest.getDomainName(), nodeConnectionState);
        }

        return ConnectionStateResponse.builder()
                .nodeConnectionState(stateQuery.getNodeConnectionState())
                .nodeProcessingState(stateQuery.getProcessingState())
                .nodeIp(easeCIWorkerProperties.getIp())
                .nodePort(easeCIWorkerProperties.getPort())
                .domainName(easeCIWorkerProperties.getName())
                .nodeName(easeCIWorkerProperties.getName())
                .transferProtocol(easeCIWorkerProperties.getTransferProtocol())
                .nodeId(easeCIWorkerProperties.getId())
                .build();
    }

    public NodeProcessingState startProcessingPipeline() {
        return currentStateHolder.busy();
    }
}
