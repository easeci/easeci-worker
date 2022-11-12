package io.easeci.worker.info;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

@Singleton
public class WorkerNodeInfoService {

    @Value("${easeci.worker-node-label}")
    private String workerNodeLabel;

    public InformationResponse getInfo() {
        return InformationResponse.builder()
                .workerNodeLabel(workerNodeLabel)
                .build();
    }
}
