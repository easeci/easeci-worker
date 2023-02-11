package io.easeci.worker.api.environment;

import io.easeci.worker.engine.docker.DockerConnectionState;

record DockerConnectionStateResponse(DockerConnectionState dockerConnectionState) {
}
