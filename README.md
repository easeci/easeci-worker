## How to build docker image via gradle wrapper:
`$ ./gradlew dockerBuild`
`$ docker tag easeci-worker:latest easeci-worker:0.0.1`

## How to run it as docker container for test purpose:
```
$ docker run --rm --name easeci-worker \
    -e WORKER_NODE_NAME="worker-node-01-example" \
    -e WORKER_NODE_IP="14.212.23.29" \
    -e WORKER_NODE_TRANSFER_PROTOCOL="HTTPS" \
    -e WORKER_NODE_PORT="443" \
    -e WORKER_NODE_DOMAIN_NAME="easeci.com" \
    -p 9001:9001 easeci-worker:0.0.1`
```

## How to run it as docker container:
```
$ docker run --rm -d --name easeci-worker
    -e WORKER_NODE_NAME="worker-node-01-example" \
    -e WORKER_NODE_IP="14.212.23.29" \
    -e WORKER_NODE_TRANSFER_PROTOCOL="HTTPS" \
    -e WORKER_NODE_PORT="443" \
    -e WORKER_NODE_DOMAIN_NAME="easeci.com" \
    -p 9001:9001 easeci-worker:0.0.1
 ```

## Docker compose useful for test many instances, removes containers after docker-compose stop:
`docker-compose up -d`
`docker-compose stop`