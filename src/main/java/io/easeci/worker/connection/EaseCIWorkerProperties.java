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
@ConfigurationProperties("easeci.worker-node")
public class EaseCIWorkerProperties {

    @NotNull
    private String name;

    @NotNull
    private UUID id;

    @NotNull
    private TransferProtocol transferProtocol;

    @NotNull
    private String ip;

    @NotNull
    private String port;

    @NotNull
    private String domainName;

}
