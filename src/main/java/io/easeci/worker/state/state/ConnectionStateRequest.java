package io.easeci.worker.state.state;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

@Data
@Introspected
class ConnectionStateRequest {
    private String nodeIp;
    private String nodePort;
    private String domainName;
    private String nodeName;
    private TransferProtocol transferProtocol;
}
