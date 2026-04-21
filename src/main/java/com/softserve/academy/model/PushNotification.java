package com.softserve.academy.model;

import com.softserve.academy.exception.NotDeliverableException;

public class PushNotification extends Notification {
    private String deviceToken;
    private String iconUrl;

    public PushNotification(String recipient, String message, int priority, String deviceToken, String iconUrl) {
        super(recipient, message, priority);
        // TODO: Ініціалізація додаткових полів
    }

    @Override
    public boolean isDeliverable() {
        // TODO: deviceToken не blank і довжина > 10
        return false;
    }

    public boolean isSilent() {
        // TODO: true якщо message порожнє (тільки тайтл)
        return false;
    }

    @Override
    public String getFormattedMessage() {
        // TODO: 🔔 {message} (якщо silent -> 🔔 (silent))
        return null;
    }

    @Override
    public int estimateDeliverySeconds() {
        // TODO: 1
        return 0;
    }

    @Override
    protected void performSend() {
        // TODO: Симуляція відправки (println)
    }

    public String getDeviceToken() { return deviceToken; }
    public String getIconUrl() { return iconUrl; }
}
