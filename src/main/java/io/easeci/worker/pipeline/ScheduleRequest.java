package io.easeci.worker.pipeline;

import io.micronaut.core.annotation.Introspected;
import lombok.*;

import java.net.URL;
import java.util.UUID;

@Introspected
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "scriptEncoded")
@AllArgsConstructor(staticName = "of")
public class ScheduleRequest {
    private UUID pipelineContextId;
    private String scriptEncoded;
    private Metadata metadata;

    @Setter
    @Getter
    @NoArgsConstructor
    @ToString
    @AllArgsConstructor(staticName = "of")
    public static class Metadata {
        private String masterNodeName;
        private String masterApplicationVersion;
        private UUID masterNodeUuid;
        private String masterApiVersion;
        private String masterApiVersionPrefix;
        private TransferProtocol transferProtocol;
        private URL logUrl;
    }
}
