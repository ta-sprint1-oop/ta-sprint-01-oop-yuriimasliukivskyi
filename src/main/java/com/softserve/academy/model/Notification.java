package com.softserve.academy.model;

import com.softserve.academy.exception.InvalidNotificationException;
import com.softserve.academy.exception.NotDeliverableException;

public abstract class Notification implements Comparable<Notification> {
    protected String recipient;
    protected String message;
    protected int priority;
    protected NotificationStatus status;

    public Notification(String recipient, String message, int priority) {
        // TODO: Базова валідація в конструкторі:
        // порожній отримувач -> InvalidNotificationException
        // порожнє повідомлення (null) -> InvalidNotificationException
        // priority від 1 до 5, інакше -> InvalidNotificationException
    }

    public abstract boolean isDeliverable();

    public abstract String getFormattedMessage();

    public abstract int estimateDeliverySeconds();

    public boolean isHighPriority() {
        // TODO: Пріоритет >= 4
        return false;
    }

    public void send() throws NotDeliverableException {
        // TODO: Шаблонний метод (Template Method):
        // 1. Перевірка isDeliverable()
        // 2. Якщо !isDeliverable() -> статус FAILED та throw NotDeliverableException
        // 3. performSend()
        // 4. Успіх -> статус SENT
    }

    protected abstract void performSend() throws NotDeliverableException;

    @Override
    public int compareTo(Notification other) {
        // TODO: Сортування за priority descending
        return 0;
    }

    // Getters
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public int getPriority() { return priority; }
    public NotificationStatus getStatus() { return status; }
}
