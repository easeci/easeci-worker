package io.easeci.commons.observer;

import java.time.LocalDateTime;

record Notification(LocalDateTime occurredOn, String content) {
}
