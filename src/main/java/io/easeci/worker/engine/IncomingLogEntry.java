package io.easeci.worker.engine;

public record IncomingLogEntry(String title,
                               String content,
                               String header,
                               long timestamp) {
}
