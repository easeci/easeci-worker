package io.easeci.worker.engine.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import io.easeci.commons.observer.Publisher;
import io.easeci.worker.engine.*;
import io.easeci.worker.pipeline.PipelineState;
import io.easeci.worker.properties.DockerProperties;
import io.easeci.worker.properties.EaseCIWorkerProperties;
import io.easeci.worker.state.state.CurrentStateHolder;
import io.micronaut.context.annotation.Context;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.netty.DefaultHttpClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static io.easeci.worker.engine.docker.EventRequestConsumerFactory.blockingHttpEventRequestConsumer;

@Slf4j
@Context
public class DockerPlatformRunner extends Publisher<PipelineStateEvent> implements Runner {

    private static final String DEFAULT_IMAGE_NAME = "easeci-default";
    private static final String CONTAINER_NAME_PREFIX = "easeci-debian-base";
    private final CurrentStateHolder currentStateHolder;
    private final HttpClient httpClient;
    private final DockerClient dockerClient;
    private final DockerProperties dockerProperties;
    private final EaseCIWorkerProperties easeCIWorkerProperties;

    public DockerPlatformRunner(CurrentStateHolder currentStateHolder, DockerClientProvider dockerClientProvider,
                                DockerProperties dockerProperties, EaseCIWorkerProperties easeCIWorkerProperties) {
        this.currentStateHolder = currentStateHolder;
        this.httpClient = new DefaultHttpClient();
        this.dockerClient = dockerClientProvider.defaultDockerClient();
        this.dockerProperties = dockerProperties;
        this.easeCIWorkerProperties = easeCIWorkerProperties;
    }

    @PostConstruct
    public void setup() {
        log.info("DockerPlatform setup started");
        DockerfileResourcesLoader dockerfileResourcesLoader = new DockerfileResourcesLoader();
        DockerBuildResultCallback dockerBuildResultCallback = new DockerBuildResultCallback();
        DockerImageHolder dockerImageHolder = new DockerImageHolder(dockerClient, dockerProperties, dockerfileResourcesLoader, dockerBuildResultCallback);
        dockerImageHolder.initializeRequiredImages();
        addSubscriber(currentStateHolder);
        log.info("Docker Platform is ready");
    }

    @Override
    public void runContainer(PipelineProcessingEnvironment pipelineProcessingEnvironment) {
        log.info("Runner just received Pipeline to run with pipelineContextId: {}", pipelineProcessingEnvironment.getPipelineContextId());
        runPipelineEvent();

        Image image = findImageByName(pipelineProcessingEnvironment.getProcessingEnvironmentName())
                .orElseGet(() -> findImageByName(DEFAULT_IMAGE_NAME)
                        .orElseThrow(() -> new ProcessingEnvironmentException("Cannot find any environment to processing pipeline with pipelineContextId: " + pipelineProcessingEnvironment.getPipelineContextId())));

        log.info("Image chosen for pipeline processing: {}", image);

        CreateContainerCmd containerCmd = createContainerCmd(pipelineProcessingEnvironment.getMountPoint(), pipelineProcessingEnvironment.getPipelineContextId(), image);
        log.info("Mounted path as docker volume: {}", pipelineProcessingEnvironment.getMountPoint());

        CreateContainerResponse createContainerResponse = containerCmd.exec();
        String containerId = createContainerResponse.getId();

        log.info("Created container with id: {}", createContainerResponse);

        StartContainerCmd startContainerCmd = dockerClient.startContainerCmd(createContainerResponse.getId());
        startContainerCmd.exec();

        LogContainerCmd logContainerCmd = logContainerCmd(containerId);

        Consumer<EventRequest> eventRequestConsumer = blockingHttpEventRequestConsumer(httpClient, pipelineProcessingEnvironment.getUrls().httpLogUrl());

        EventRequestHandler eventRequestHandler = new EventRequestHandler(eventRequestConsumer);
        DockerPipelineRunResultCallback dockerPipelineRunResultCallback = new DockerPipelineRunResultCallback(pipelineProcessingEnvironment.getPipelineContextId(),
                containerId, eventRequestHandler, easeCIWorkerProperties, this::finishedPipelineEvent);

        logContainerCmd.exec(dockerPipelineRunResultCallback);
    }

    private Optional<Image> findImageByName(String imageName) {
        return dockerProperties.getPredefinedImages().stream()
                .filter(image -> image.getName().equals(imageName))
                .findFirst();
    }

    private LogContainerCmd logContainerCmd(String containerId) {
        return dockerClient.logContainerCmd(containerId)
                .withStdOut(true)
                .withStdErr(true)
                .withSince(0)
                .withFollowStream(true)
                .withTimestamps(false);
    }

    private CreateContainerCmd createContainerCmd(Path mountPoint, UUID pipelineContextId, Image image) {
        return dockerClient.createContainerCmd(image.reference())
                .withVolumes(new Volume(mountPoint.toString()))
                .withHostConfig(HostConfig.newHostConfig().withBinds(new Bind(mountPoint.toString(), new Volume(mountPoint.toString()))))
                .withName(CONTAINER_NAME_PREFIX.concat(pipelineContextId.toString()))
                .withTty(true)
                .withCmd(prepareContainerCommand(mountPoint));
    }

    private String[] prepareContainerCommand(Path mountPoint) {
        return new String[]{
                "python3",
                mountPoint.toString()
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
