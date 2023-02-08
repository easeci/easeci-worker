package io.easeci.worker.state.state;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
class ConnectionStateResponse {
    private NodeConnectionState nodeConnectionState;
    private NodeProcessingState nodeProcessingState;
    private String nodeIp;
    private String nodePort;
    private String domainName;
    private String nodeName;
    private UUID nodeId;
    private TransferProtocol transferProtocol;
}
