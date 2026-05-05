package com.softserve.academy.model;

import com.softserve.academy.exception.NotDeliverableException;

public class SmsNotification extends Notification {
    private String phoneNumber;
    private boolean isFlash;

    public SmsNotification(String recipient, String message, int priority, String phoneNumber, boolean isFlash) {
        super(recipient, message, priority);
        this.phoneNumber = phoneNumber;
        this.isFlash = isFlash;
    }

    @Override
    public boolean isDeliverable() {
        return recipient != null && 
        recipient.startsWith("+") && 
        recipient.length() >= 10 && 
        recipient.length() <= 15;
    }

    public boolean isOverLimit() {
        return message != null && message.length() > 160;
    }

    @Override
    public String getFormattedMessage() {
        if (message == null) return "";

        if (message.length() > 160) {
            return message.substring(0, 160);
        }
        return message;
    }

    @Override
    public int estimateDeliverySeconds() {
        return 5;
    }

    @Override
    protected void performSend() {
        System.out.println("Sending SMS to " + recipient + ": " + getFormattedMessage());
    }

    public String getPhoneNumber() { return phoneNumber; }
    public boolean isFlash() { return isFlash; }
}
