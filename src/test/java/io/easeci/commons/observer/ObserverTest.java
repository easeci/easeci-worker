package io.easeci.commons.observer;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ObserverTest {

    @Test
    @DisplayName("Should subscribers of YT channel be called about notification")
    void observerTest() {
        YouTube youTube = new YouTube();

        Subscriber<Notification> userA = new User();
        Subscriber<Notification> userB = new User();
        Subscriber<Notification> userC = new User();
        Subscriber<Notification> userD = new User();
        Subscriber<Notification> userE = new User();

        youTube.addSubscriber(userA);
        youTube.addSubscriber(userB);
        youTube.addSubscriber(userC);
        youTube.addSubscriber(userD);
        youTube.addSubscriber(userE);

        Notification notification = new Notification(LocalDateTime.now(), "New movie was released on your favourite channel!");
        youTube.notifySubscribers(notification);

        assertAll(
                () -> assertEquals(notification, ((User) userA).getLastNotification()),
                () -> assertEquals(notification, ((User) userB).getLastNotification()),
                () -> assertEquals(notification, ((User) userC).getLastNotification()),
                () -> assertEquals(notification, ((User) userD).getLastNotification()),
                () -> assertEquals(notification, ((User) userE).getLastNotification())
        );
    }

    @Test
    @DisplayName("Should correctly remove subscriber")
    void observerSubscriberRemovalTest() {
        YouTube youTube = new YouTube();

        Subscriber<Notification> userA = new User();
        Subscriber<Notification> userB = new User();
        Subscriber<Notification> userC = new User();
        Subscriber<Notification> userD = new User();
        Subscriber<Notification> userE = new User();

        youTube.addSubscriber(userA);
        youTube.addSubscriber(userB);
        youTube.addSubscriber(userC);
        youTube.addSubscriber(userD);
        youTube.addSubscriber(userE);

        Notification notification = new Notification(LocalDateTime.now(), "New movie was released on your favourite channel!");

        boolean isRemoved = youTube.removeSubscriber(userE);

        youTube.notifySubscribers(notification);

        assertAll(
                () -> assertTrue(isRemoved),
                () -> assertEquals(notification, ((User) userA).getLastNotification()),
                () -> assertEquals(notification, ((User) userB).getLastNotification()),
                () -> assertEquals(notification, ((User) userC).getLastNotification()),
                () -> assertEquals(notification, ((User) userD).getLastNotification()),
                () -> assertNull(((User) userE).getLastNotification())
        );
    }

}
