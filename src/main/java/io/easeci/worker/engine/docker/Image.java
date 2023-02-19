package io.easeci.worker.engine.docker;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Image {
    private String name;
    private String repository;
    private String tag;
    private String resourcesPath;

    public String reference() {
        return repository + ":" + tag;
    }
}
