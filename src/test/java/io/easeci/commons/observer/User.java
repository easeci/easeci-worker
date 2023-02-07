package io.easeci.commons.observer;

import java.util.ArrayList;
import java.util.List;

class User implements Subscriber<Notification> {

    private final List<Notification> notifications = new ArrayList<>();

    @Override
    public void update(Notification notification) {
        notifications.add(notification);
    }

    public Notification getLastNotification() {
        if (notifications.isEmpty()) {
            return null;
        }
        return notifications.get(notifications.size() - 1);
    }
}
