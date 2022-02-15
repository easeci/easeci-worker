package io.easeci.worker.connect.state;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import lombok.AllArgsConstructor;

import javax.validation.Valid;

@AllArgsConstructor
@Controller("/api/v1/connection/state")
public class ConnectionStateController {

    private ConnectionStateService connectionStateService;

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<ConnectionStateResponse> checkConnectionState(@Body @Valid ConnectionStateRequest connectionStateRequest) {
        return HttpResponse.ok(connectionStateService.checkConnectionState(connectionStateRequest));
    }
}
