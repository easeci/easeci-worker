package io.easeci.worker.connect.state;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Controller("/api/v1/connection/state")
public class ConnectionStateController {

    private ConnectionStateService connectionStateService;

    @Post
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<ConnectionStateResponse> checkConnectionState(@Body @Valid ConnectionStateRequest connectionStateRequest) {
        log.info("Request for check connection state occurred from: {}", connectionStateRequest.getNodeIp());
        return HttpResponse.ok(connectionStateService.checkConnectionState(connectionStateRequest));
    }
}
