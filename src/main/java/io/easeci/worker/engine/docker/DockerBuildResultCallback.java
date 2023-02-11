package io.easeci.worker.engine.docker;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.BuildResponseItem;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;

@Slf4j
class DockerBuildResultCallback implements ResultCallback<BuildResponseItem> {

    @Override
    public void onStart(Closeable closeable) {
        log.info("Starting building docker image");
    }

    @Override
    public void onNext(BuildResponseItem object) {
        final String whale = "🐳 ";
        object.getRawValues().forEach((string, value) -> {
            if (!value.toString().isBlank()) {
                log.info(whale + value.toString().trim());
            }
        });
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Error occurred while building docker image:", throwable);
    }

    @Override
    public void onComplete() {
        log.info("Docker image build is completed");
    }

    @Override
    public void close() throws IOException {
        log.info("Closing of result callback of build docker image");
    }
}
