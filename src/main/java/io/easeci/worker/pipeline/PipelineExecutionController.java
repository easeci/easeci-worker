package io.easeci.worker.pipeline;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
@Controller("/api/v1/pipeline/receive")
public class PipelineExecutionController {

    private PipelineExecutionFacade pipelineExecutionFacade;

    @Post
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<ScheduleResponse> handlePipelineExecution(@Body @Valid ScheduleRequest scheduleRequest) throws IOException {
        log.info("Request for schedule pipeline job occurred: {}", scheduleRequest);
        return HttpResponse.ok(pipelineExecutionFacade.handlePipeline(scheduleRequest));
    }
}
