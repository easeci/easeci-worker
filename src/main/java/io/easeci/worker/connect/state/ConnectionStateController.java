package io.easeci.worker.connect.state;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.server.util.HttpClientAddressResolver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Controller("/api/v1/connection/state")
public class ConnectionStateController {

    private ConnectionStateService connectionStateService;
    private ProcessingStateService processingStateService;
    private final HttpClientAddressResolver httpClientAddressResolver;

    @Post
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<ConnectionStateResponse> checkConnectionState(@Body @Valid ConnectionStateRequest connectionStateRequest) {
        log.info("Request for check connection state occurred from: {}", connectionStateRequest.getNodeIp());
        return HttpResponse.ok(connectionStateService.checkConnectionState(connectionStateRequest));
    }

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<ProcessingStateResponse> checkProcessingState(HttpRequest<?> httpRequest) {
        log.info("Request for check processing state occurred from: {}", httpClientAddressResolver.resolve(httpRequest));
        return HttpResponse.ok(processingStateService.checkProcessingState());
    }
}
