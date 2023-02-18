package io.easeci.worker.engine;

import lombok.Data;

@Data
public class Image {
    private String name;
    private String repository;
    private String tag;
    private String resourcesPath;

    public String reference() {
        return repository + ":" + tag;
    }
}
