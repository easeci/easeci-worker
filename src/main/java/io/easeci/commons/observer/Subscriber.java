package io.easeci.commons.observer;

public interface Subscriber<T> {

    void update(T context);
}
