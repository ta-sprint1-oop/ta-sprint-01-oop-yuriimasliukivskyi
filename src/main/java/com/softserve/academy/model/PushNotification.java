package com.softserve.academy.model;

import com.softserve.academy.exception.NotDeliverableException;

public class PushNotification extends Notification {
    private String deviceToken;
    private String iconUrl;

    public PushNotification(String recipient, String message, int priority, String deviceToken, String iconUrl) {
        super(recipient, message, priority);
        this.deviceToken = deviceToken;
        this.iconUrl = iconUrl;
    }

    @Override
    public boolean isDeliverable() {
        return deviceToken != null && !deviceToken.isBlank() && deviceToken.length() > 10;
    }

    public boolean isSilent() {
        return message.isBlank();
    }

    @Override
    public String getFormattedMessage() {
        if (isSilent()) {
        return "🔔 (silent)";
        }
        return "🔔 " + message;
    }

    @Override
    public int estimateDeliverySeconds() {
        return 1;
    }

    @Override
    protected void performSend() {
        System.out.println("Sending PUSH to " + recipient + ": " + getFormattedMessage());
    }

    public String getDeviceToken() { return deviceToken; }
    public String getIconUrl() { return iconUrl; }
}
