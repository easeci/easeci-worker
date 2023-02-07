package io.easeci.worker.connection.info;

import io.easeci.worker.connection.EaseCIWorkerProperties;
import io.easeci.worker.connection.state.StateQuery;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class WorkerNodeInfoService {
    private final EaseCIWorkerProperties easeCIWorkerProperties;
    private final StateQuery stateQuery;

    public InformationResponse getInfo() {
        return InformationResponse.builder()
                .nodeConnectionState(stateQuery.getNodeConnectionState())
                .nodeProcessingState(stateQuery.getProcessingState())
                .nodeIp(easeCIWorkerProperties.getIp())
                .nodePort(easeCIWorkerProperties.getPort())
                .domainName(easeCIWorkerProperties.getDomainName())
                .nodeName(easeCIWorkerProperties.getName())
                .transferProtocol(easeCIWorkerProperties.getTransferProtocol())
                .nodeId(easeCIWorkerProperties.getId())
                .build();
    }
}
