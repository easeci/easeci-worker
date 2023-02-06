package io.easeci.worker.connection;

import io.easeci.worker.connection.state.TransferProtocol;
import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Data;
import lombok.ToString;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Data
@ToString
@Valid
@ConfigurationProperties("easeci")
public class EaseCIWorkerProperties {

    @NotNull
    private String workerNodeName;

    @NotNull
    private UUID workerNodeId;

    @NotNull
    private TransferProtocol workerNodeTransferProtocol;

    @NotNull
    private String workerNodeIp;

    @NotNull
    private String workerNodePort;

    @NotNull
    private String workerNodeDomainName;

}
