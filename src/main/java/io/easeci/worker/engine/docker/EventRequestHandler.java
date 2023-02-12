package io.easeci.worker.engine.docker;

import io.easeci.worker.engine.EventRequest;
import io.easeci.worker.engine.IncomingLogEntry;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@RequiredArgsConstructor
class EventRequestHandler {

    private final Consumer<EventRequest> eventRequestConsumer;

    public void handleEventRequest(EventRequest eventRequest) {
        eventRequestConsumer.accept(eventRequest);
    }

    static class EventFactory {

        private static final String HEADER_INFO = "INFO";

        static EventRequest factorizeInfoEvent(UUID pipelineContextId, String workerNodeName, String payload) {
            return new EventRequest(pipelineContextId, null, workerNodeName,
                    List.of(new IncomingLogEntry("", payload, HEADER_INFO, Instant.now().getEpochSecond())));
        }
    }
}
