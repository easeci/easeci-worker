package io.easeci.worker.connect.health;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/api/v1/health-check")
public class HealthCheckController {

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public HealthCheckResponse healthCheck() {
        return new HealthCheckResponse("I'm OK");
    }
}
