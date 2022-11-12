package io.easeci.worker.connect.state;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ConnectionStateResponse {
    private NodeConnectionState nodeConnectionState;
    private NodeProcessingState nodeProcessingState;
    private String nodeIp;
    private String nodePort;
    private String domainName;
    private String nodeName;
    private TransferProtocol transferProtocol;
}
