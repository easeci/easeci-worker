package io.easeci.worker.engine;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

@Slf4j
public class DockerPlatformRunner implements Runner {

    @Override
    public void execution(File file) {
        DockerClient dockerClient = constructClient();
//        InputStreamReader inputStreamReader = new InputStreamReader(response.getBody());
//        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//        bufferedReader.lines().forEach(System.out::println);

        System.out.println("-------------- CONNECTION TO DOCKER IS OK-----------------");

        List<Image> images = dockerClient.listImagesCmd().exec();
        images.stream().forEach(image -> System.out.println(image.toString()));

        dockerClient.pingCmd().exec();


    }

    private DockerClient constructClient() {
        DockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .build();
        log.info("Docker connection created with API Version: {}", dockerClientConfig.getApiVersion().getVersion());

        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(dockerClientConfig.getDockerHost())
                .sslConfig(dockerClientConfig.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

        DockerHttpClient.Response response = httpClient.execute(DockerHttpClient.Request.builder()
                .method(DockerHttpClient.Request.Method.GET).path("/version")
                .build());
        DockerClient dockerClient = DockerClientImpl.getInstance(dockerClientConfig, httpClient);
        return dockerClient;
    }

    public void fetchBuildContainer() {
        DockerClient dockerClient = constructClient();
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

    public void runContainer(Path mountPoint) {
        // create container
        DockerClient dockerClient = constructClient();
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd("easeci-debian-base:v1.0")
                .withVolumes(new Volume(mountPoint.toString()))
                .withHostConfig(HostConfig.newHostConfig().withBinds(new Bind(mountPoint.toString(), new Volume(mountPoint.toString()))))
                .withName("easeci-debian-base") // todo walidacja jeżeli nazwa istnieje
                .withCmd("python3", mountPoint.toString().concat("/pipeline-script.py"));
        log.info("Mounted path: {}", mountPoint);
        CreateContainerResponse container = containerCmd.exec();
        String id = container.getId();
        System.out.println("Container id: " +  id);

        StartContainerCmd startContainerCmd = dockerClient.startContainerCmd(container.getId());
        startContainerCmd.exec();

        InspectContainerCmd inspectContainerCmd = dockerClient.inspectContainerCmd(id);
        InspectContainerResponse inspectContainerResponse = inspectContainerCmd.exec();
        InspectContainerResponse.ContainerState state = inspectContainerResponse.getState();
        System.out.println(state.getStatus());

        dockerClient.removeContainerCmd("easeci-debian-base");
        log.info("Container finished his job end is removed now");
    }
}
