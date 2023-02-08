package io.easeci.worker.state.info;

import io.easeci.worker.state.EaseCIWorkerProperties;
import io.easeci.worker.state.state.StateQuery;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
class WorkerNodeInfoService {
    private final EaseCIWorkerProperties easeCIWorkerProperties;
    private final StateQuery stateQuery;

    InformationResponse getInfo() {
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
