package io.easeci.worker.connect.state;

import jakarta.inject.Singleton;

import java.util.Date;

@Singleton
public class ProcessingStateService {

    // todo mock response
    public ProcessingStateResponse checkProcessingState() {
        return ProcessingStateResponse.builder()
                                      .nodeProcessingState(NodeProcessingState.IDLE)
                                      .nodeProcessingStateCheckDate(new Date())
                                      .build();
    }
}
