package io.easeci.worker.engine;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import io.easeci.commons.observer.Publisher;
import io.easeci.worker.state.state.CurrentStateHolder;
import io.easeci.worker.pipeline.PipelineState;
import io.micronaut.context.annotation.Context;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.netty.DefaultHttpClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Context
@RequiredArgsConstructor
public class DockerPlatformRunner extends Publisher<PipelineStateEvent> implements Runner {

    private final CurrentStateHolder currentStateHolder;
    private final HttpClient httpClient;
    private final DockerClient dockerClient;

    public DockerPlatformRunner(CurrentStateHolder currentStateHolder, DockerClientProvider dockerClientProvider) {
        this.currentStateHolder = currentStateHolder;
        this.httpClient = new DefaultHttpClient();
        this.dockerClient = dockerClientProvider.defaultDockerClient();
    }

    @PostConstruct
    public void setup() {
        addSubscriber(currentStateHolder);
    }

    @Override
    public void execution(File file) {
        addSubscriber(currentStateHolder);
//        InputStreamReader inputStreamReader = new InputStreamReader(response.getBody());
//        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//        bufferedReader.lines().forEach(System.out::println);

        System.out.println("-------------- CONNECTION TO DOCKER IS OK-----------------");

        List<Image> images = dockerClient.listImagesCmd().exec();
        images.stream().forEach(image -> System.out.println(image.toString()));

        dockerClient.pingCmd().exec();


    }

    public void fetchBuildContainer() {
        PullImageCmd pullImageCmd = dockerClient.pullImageCmd("debian")
                .withRepository("debian")
                .withTag("stable-slim");
        pullImageCmd.exec(new ResultCallback<PullResponseItem>() {
            @Override
            public void onStart(Closeable closeable) {
                log.info("Pulling image");
            }

            @Override
            public void onNext(PullResponseItem object) {
                log.info(object.getStatus());
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                log.info("Pulling image completed");
            }

            @Override
            public void close() throws IOException {
                log.info("Closed command");
            }
        });
    }

    public void runContainer(Path mountPoint, UUID pipelineContextId) {
        log.info("Runner just received Pipeline to run with pipelineContextId: {}", pipelineContextId);
        runPipelineEvent();

        // create container
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd("easeci-debian-base:v1.0")
                .withVolumes(new Volume(mountPoint.toString()))
                .withHostConfig(HostConfig.newHostConfig().withBinds(new Bind(mountPoint.toString(), new Volume(mountPoint.toString()))))
                .withName("easeci-debian-base".concat(pipelineContextId.toString()))
//                .withCmd("python3", mountPoint.toString().concat("/pipeline-script.py"));
                .withTty(true)
                .withCmd("python3", mountPoint.toString().concat("/pipeline-script.py"));
        log.info("Mounted path: {}", mountPoint);
        CreateContainerResponse container = containerCmd.exec();
        String id = container.getId();
        System.out.println("Container id: " +  id);

        StartContainerCmd startContainerCmd = dockerClient.startContainerCmd(container.getId());
        startContainerCmd.exec();

        LogContainerCmd logContainerCmd = dockerClient.logContainerCmd(id)
                .withStdOut(true)
                .withStdErr(true)
                .withSince(0)
                .withFollowStream(true)
                .withTimestamps(false);

        logContainerCmd.exec(resultCallback(id, pipelineContextId));


        InspectContainerCmd inspectContainerCmd = dockerClient.inspectContainerCmd(id);
        InspectContainerResponse inspectContainerResponse = inspectContainerCmd.exec();
        InspectContainerResponse.ContainerState state = inspectContainerResponse.getState();
        System.out.println(state.getStatus());
    }

    private ResultCallback<Frame> resultCallback(String containerId, UUID pipelineContextId) {
        return new ResultCallback<Frame>() {
            @Override
            public void onStart(Closeable closeable) {
                log.info("Start listening logs of container: {}", containerId);
            }

            @Override
            public void onNext(Frame object) {

                log.info(new String(object.getPayload()));

                EventRequest eventRequest = new EventRequest(pipelineContextId, UUID.randomUUID(), "worker-node-01",
                        List.of(new IncomingLogEntry("Log", new String(object.getPayload()), "INFO", Instant.now().getEpochSecond())));

                Mono.from(httpClient.exchange(HttpRequest.POST("http://localhost:9000/api/v1/ws/logs", eventRequest)))
                        .block();
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                log.info("Pipeline processing ends");
                finishedPipelineEvent();
            }

            @Override
            public void close() throws IOException {
                log.info("Closing mechanism of listening logs of container: {}", containerId);
            }
        };
    }

    private void runPipelineEvent() {
        PipelineStateEvent pipelineStateEvent = new PipelineStateEvent(PipelineState.RUN);
        notifySubscribers(pipelineStateEvent);
    }

    private void finishedPipelineEvent() {
        PipelineStateEvent pipelineStateEvent = new PipelineStateEvent(PipelineState.FINISHED);
        notifySubscribers(pipelineStateEvent);
    }
}
