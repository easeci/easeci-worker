package io.easeci.worker.pipeline;

import io.micronaut.core.annotation.Introspected;
import lombok.*;

import java.util.UUID;

@Introspected
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "scriptEncoded")
public class ScheduleRequest {
    private UUID pipelineContextId;
    private String scriptEncoded;
    private Metadata metadata;
    private Environment environment;

    @Setter
    @Getter
    @NoArgsConstructor
    @ToString
    public static class Metadata {
        private String masterNodeName;
        private String masterApplicationVersion;
        private UUID masterNodeUuid;
        private String masterApiVersion;
        private String masterApiVersionPrefix;
        private TransferProtocol transferProtocol;
        private Urls urls;
    }

    @Data
    @ToString
    public static class Environment {
        private String name;
    }
}
