package io.easeci.worker.connect.state;

import lombok.Data;

@Data
public class ConnectionStateRequest {
    private String nodeIp;
    private String nodePort;
    private String domainName;
    private String nodeName;
    private TransferProtocol transferProtocol;
}
