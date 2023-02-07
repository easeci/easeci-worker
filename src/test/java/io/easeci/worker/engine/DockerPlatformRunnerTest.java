package io.easeci.worker.engine;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.Volume;
import io.easeci.worker.connection.state.CurrentStateHolder;
import io.easeci.worker.connection.state.NodeProcessingState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class DockerPlatformRunnerTest {

    private final String containerId = UUID.randomUUID().toString();

    @Test
    @DisplayName("Should correctly subscribe to DockerPlatformRunner and receive correctly status update")
    void subscriptionTest() {
        CurrentStateHolder currentStateHolder = new CurrentStateHolder();
        CreateContainerCmd createContainerCmd = mockCreateContainerCmd();
        LogContainerCmd logContainerCmd = mockLogContainerCmd();
        DockerClient dockerClient = Mockito.mock(DockerClient.class);
        mockInspectContainerCmd(dockerClient);
        Mockito.when(dockerClient.createContainerCmd(anyString())).thenReturn(createContainerCmd);
        Mockito.when(dockerClient.startContainerCmd(containerId)).thenReturn(mockStartContainerCmd());
        Mockito.when(dockerClient.logContainerCmd(anyString())).thenReturn(logContainerCmd);
        DockerPlatformRunner dockerPlatformRunner = new DockerPlatformRunner(currentStateHolder, dockerClient);

        dockerPlatformRunner.setup();
        dockerPlatformRunner.runContainer(Paths.get("/"), UUID.randomUUID());

        NodeProcessingState processingState = currentStateHolder.getProcessingState();
        assertEquals(NodeProcessingState.BUSY, processingState);
    }

    StartContainerCmd mockStartContainerCmd() {
        return Mockito.mock(StartContainerCmd.class);
    }

    CreateContainerCmd mockCreateContainerCmd() {
        CreateContainerCmd createContainerCmd = Mockito.mock(CreateContainerCmd.class);
        Mockito.when(createContainerCmd.withName(any())).thenReturn(createContainerCmd);
        Mockito.when(createContainerCmd.withVolumes(any(Volume.class))).thenReturn(createContainerCmd);
        Mockito.when(createContainerCmd.withHostConfig(any())).thenReturn(createContainerCmd);
        Mockito.when(createContainerCmd.withTty(any())).thenReturn(createContainerCmd);
        Mockito.when(createContainerCmd.withCmd(anyString(), anyString())).thenReturn(createContainerCmd);
        CreateContainerResponse createContainerResponse = Mockito.mock(CreateContainerResponse.class);
        Mockito.when(createContainerResponse.getId()).thenReturn(this.containerId);
        Mockito.when(createContainerCmd.exec()).thenReturn(createContainerResponse);
        return createContainerCmd;
    }

    LogContainerCmd mockLogContainerCmd() {
        LogContainerCmd logContainerCmd = Mockito.mock(LogContainerCmd.class);
        Mockito.when(logContainerCmd.withStdOut(any())).thenReturn(logContainerCmd);
        Mockito.when(logContainerCmd.withStdErr(any())).thenReturn(logContainerCmd);
        Mockito.when(logContainerCmd.withSince(any())).thenReturn(logContainerCmd);
        Mockito.when(logContainerCmd.withFollowStream(any())).thenReturn(logContainerCmd);
        Mockito.when(logContainerCmd.withTimestamps(any())).thenReturn(logContainerCmd);
        Mockito.when(logContainerCmd.exec(any())).thenReturn(Mockito.mock(ResultCallback.class));
        return logContainerCmd;
    }

    InspectContainerCmd mockInspectContainerCmd(DockerClient dockerClient) {
        InspectContainerResponse inspectContainerResponse = Mockito.mock(InspectContainerResponse.class);
        InspectContainerCmd inspectContainerCmd = Mockito.mock(InspectContainerCmd.class);
        Mockito.when(dockerClient.inspectContainerCmd(anyString())).thenReturn(inspectContainerCmd);
        Mockito.when(inspectContainerCmd.exec()).thenReturn(inspectContainerResponse);
        Mockito.when(inspectContainerResponse.getState()).thenReturn(Mockito.mock(InspectContainerResponse.ContainerState.class));
        return inspectContainerCmd;
    }
}