package io.easeci.worker.connection.info;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Controller("/api/v1/info")
class InformationController {

    private WorkerNodeInfoService workerNodeInfoService;

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    HttpResponse<InformationResponse> workerNodeInfo() {
        log.info("Request for check information about this worker node");
        return HttpResponse.ok(workerNodeInfoService.getInfo());
    }
}
