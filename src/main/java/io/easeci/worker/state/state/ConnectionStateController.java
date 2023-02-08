package io.easeci.worker.state.state;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Controller("/api/v1/connection/state")
class ConnectionStateController {

    private ConnectionStateService connectionStateService;

    @Post
    @Produces(MediaType.APPLICATION_JSON)
    HttpResponse<ConnectionStateResponse> checkConnectionState(@Body @Valid ConnectionStateRequest connectionStateRequest) {
        return HttpResponse.ok(connectionStateService.checkConnectionState(connectionStateRequest));
    }
}
