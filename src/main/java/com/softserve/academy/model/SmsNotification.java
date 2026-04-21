package com.softserve.academy.model;

import com.softserve.academy.exception.NotDeliverableException;

public class SmsNotification extends Notification {
    private String phoneNumber;
    private boolean isFlash;

    public SmsNotification(String recipient, String message, int priority, String phoneNumber, boolean isFlash) {
        super(recipient, message, priority);
        // TODO: Ініціалізація додаткових полів
    }

    @Override
    public boolean isDeliverable() {
        // TODO: Номер починається з + і має довжину 10-15 символів
        return false;
    }

    public boolean isOverLimit() {
        // TODO: true якщо message > 160 символів
        return false;
    }

    @Override
    public String getFormattedMessage() {
        // TODO: Обрізає до 160 символів якщо довше
        return null;
    }

    @Override
    public int estimateDeliverySeconds() {
        // TODO: 5
        return 0;
    }

    @Override
    protected void performSend() {
        // TODO: Симуляція відправки (println)
    }

    public String getPhoneNumber() { return phoneNumber; }
    public boolean isFlash() { return isFlash; }
}
