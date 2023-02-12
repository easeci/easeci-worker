package io.easeci.worker.engine.docker;

import io.easeci.worker.engine.EventRequest;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

class EventRequestConsumerFactory {

    static Consumer<EventRequest> blockingHttpEventRequestConsumer(HttpClient httpClient, String masterNodeURI) {
        return eventRequest -> Mono.from(httpClient.exchange(HttpRequest.POST(masterNodeURI, eventRequest)))
                .block();
    }
}
