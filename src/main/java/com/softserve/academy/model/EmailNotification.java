package com.softserve.academy.model;

import com.softserve.academy.exception.NotDeliverableException;

import java.util.List;

public class EmailNotification extends Notification {
    private String senderEmail;
    private String subject;
    private boolean hasAttachment;

    public EmailNotification(String recipient, String message, int priority, String senderEmail, String subject, boolean hasAttachment) {
        super(recipient, message, priority);
        // TODO: Ініціалізація додаткових полів
    }

    @Override
    public boolean isDeliverable() {
        // TODO: Перевірка що email містить @ і .
        return false;
    }

    public boolean isSpam() {
        // TODO: Якщо subject містить "free", "win", "click" (case insensitive)
        return false;
    }

    @Override
    public String getFormattedMessage() {
        // TODO: Subject: {subject}\n{message}
        return null;
    }

    @Override
    public int estimateDeliverySeconds() {
        // TODO: 30
        return 0;
    }

    @Override
    protected void performSend() {
        // TODO: Симуляція відправки (println)
    }

    public String getSenderEmail() { return senderEmail; }
    public String getSubject() { return subject; }
    public boolean isHasAttachment() { return hasAttachment; }
}
