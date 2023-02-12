package io.easeci.worker.engine.docker;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;
import io.easeci.worker.engine.EventRequest;
import io.easeci.worker.properties.EaseCIWorkerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
class DockerPipelineRunResultCallback implements ResultCallback<Frame> {

    private static final String whale = "🐳 ";
    private final UUID pipelineContextId;
    private final String containerId;
    private final EventRequestHandler eventRequestHandler;
    private final EaseCIWorkerProperties easeCIWorkerProperties;
    private final Runnable onCloseFunction;

    @Override
    public void onStart(Closeable closeable) {
        log.info("Starting running container with id: {}", containerId);
    }

    @Override
    public void onNext(Frame object) {
        String logContent = new String(object.getPayload());
        log.info(whale.concat(logContent));
        EventRequest eventRequest = EventRequestHandler.EventFactory.factorizeInfoEvent(pipelineContextId, easeCIWorkerProperties.getName(), logContent);
        eventRequestHandler.handleEventRequest(eventRequest);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Error occurred while processing pipeline with pipelineContextId: {}", pipelineContextId, throwable);
    }

    @Override
    public void onComplete() {
        log.info("Completing running container with id: {}", containerId);
        onCloseFunction.run();
    }

    @Override
    public void close() throws IOException {
        log.info("Closing mechanism of listening logs of container: {}", containerId);
    }
}
