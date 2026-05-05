package com.softserve.academy.model;

import com.softserve.academy.exception.InvalidNotificationException;
import com.softserve.academy.exception.NotDeliverableException;

public abstract class Notification implements Comparable<Notification> {
    protected String recipient;
    protected String message;
    protected int priority;
    protected NotificationStatus status;

    public Notification(String recipient, String message, int priority) {
            if (recipient == null || recipient.isBlank()) {
            throw new InvalidNotificationException("Recipient cannot be empty");
        }
        if (message == null) {
            throw new InvalidNotificationException("Message cannot be null");
        }
        if (priority < 1 || priority > 5) {
            throw new InvalidNotificationException("Priority must be between 1 and 5");
        }

        this.recipient = recipient;
        this.message = message;
        this.priority = priority;
        this.status = NotificationStatus.PENDING;
    }

    public abstract boolean isDeliverable();

    public abstract String getFormattedMessage();

    public abstract int estimateDeliverySeconds();

    public boolean isHighPriority() {
        return priority >= 4;
    }

    public void send() throws NotDeliverableException {
            if (!isDeliverable()) {
        status = NotificationStatus.FAILED;
        throw new NotDeliverableException("Notification is not deliverable");
    }

    performSend(); 
    status = NotificationStatus.SENT;
    }

    protected abstract void performSend() throws NotDeliverableException;

    @Override
    public int compareTo(Notification other) {
        return Integer.compare(other.priority, this.priority);
    }

    // Getters
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public int getPriority() { return priority; }
    public NotificationStatus getStatus() { return status; }
}
