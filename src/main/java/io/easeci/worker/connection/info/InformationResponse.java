package io.easeci.worker.connection.info;

import io.easeci.worker.connection.state.NodeConnectionState;
import io.easeci.worker.connection.state.NodeProcessingState;
import io.easeci.worker.connection.state.TransferProtocol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class InformationResponse {
    private NodeConnectionState nodeConnectionState;
    private NodeProcessingState nodeProcessingState;
    private String nodeIp;
    private String nodePort;
    private String domainName;
    private String nodeName;
    private UUID nodeId;
    private TransferProtocol transferProtocol;
}
