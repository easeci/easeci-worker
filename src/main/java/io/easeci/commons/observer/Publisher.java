package io.easeci.commons.observer;

import java.util.HashSet;
import java.util.Set;

public abstract class Publisher<T> {
    private final Set<Subscriber<T>> subscribers = new HashSet<>();

    public void notifySubscribers(T context) {
        this.subscribers.forEach(subscriber -> subscriber.update(context));
    }

    public boolean addSubscriber(Subscriber<T> subscriber) {
        return this.subscribers.add(subscriber);
    }

    public boolean removeSubscriber(Subscriber<T> subscriber) {
        return this.subscribers.remove(subscriber);
    }
}
