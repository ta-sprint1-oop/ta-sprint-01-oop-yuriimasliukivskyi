package com.softserve.academy.model;

import com.softserve.academy.exception.InvalidNotificationException;
import com.softserve.academy.exception.NotDeliverableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    private static final String VALID_EMAIL = "john.doe@example.com";
    private static final String VALID_PHONE = "+380501234567";
    private static final String VALID_TOKEN = "device-token-unique-12345";
    private static final String VALID_SENDER = "noreply@softserve.ua";

    @Test
    @DisplayName("Test EmailNotification full flow and logic")
    void testEmailNotification() throws NotDeliverableException {
        EmailNotification email = new EmailNotification(VALID_EMAIL, "Your order #12345 has been shipped!", 5, VALID_SENDER, "Order Update", false);
        assertEquals(NotificationStatus.PENDING, email.getStatus());
        
        assertTrue(email.isDeliverable());
        assertFalse(email.isSpam());
        assertEquals("Subject: Order Update\nYour order #12345 has been shipped!", email.getFormattedMessage());
        assertEquals(30, email.estimateDeliverySeconds());
        assertTrue(email.isHighPriority());

        email.send();
        assertEquals(NotificationStatus.SENT, email.getStatus());
        
        EmailNotification spam = new EmailNotification(VALID_EMAIL, "Claim your prize immediately!", 3, "spammer@bad.net", "You WON a FREE gift, CLICK here", false);
        assertTrue(spam.isSpam());
        assertFalse(spam.isHighPriority());

        // Test delivery failures
        EmailNotification noAt = new EmailNotification("invalid-email-no-at", "Msg", 1, VALID_SENDER, "Sub", false);
        assertFalse(noAt.isDeliverable());
        EmailNotification noDot = new EmailNotification("user@domain-no-dot", "Msg", 1, VALID_SENDER, "Sub", false);
        assertFalse(noDot.isDeliverable());

        assertThrows(NotDeliverableException.class, noAt::send);
        assertEquals(NotificationStatus.FAILED, noAt.getStatus());
    }

    @Test
    @DisplayName("Test SmsNotification full flow and logic")
    void testSmsNotification() throws NotDeliverableException {
        SmsNotification sms = new SmsNotification("Alice Smith", "Your verification code is 5566", 4, VALID_PHONE, false);
        
        assertTrue(sms.isDeliverable());
        assertFalse(sms.isOverLimit());
        assertEquals("Your verification code is 5566", sms.getFormattedMessage());
        assertEquals(5, sms.estimateDeliverySeconds());
        assertTrue(sms.isHighPriority());

        // Test over limit
        String longMsg = "This is a very long SMS message that definitely exceeds the standard one hundred and sixty character limit imposed by the GSM network protocol to test truncation logic correctly. " + ".".repeat(20);
        SmsNotification longSms = new SmsNotification("Bob Brown", longMsg, 2, VALID_PHONE, false);
        assertTrue(longSms.isOverLimit());
        assertEquals(160, longSms.getFormattedMessage().length());
        assertTrue(longSms.getFormattedMessage().startsWith("This is a very long SMS"));

        // Test deliverability bounds for phone number
        assertTrue(new SmsNotification("U", "M", 1, "+123456789", true).isDeliverable()); // 10 chars
        assertTrue(new SmsNotification("U", "M", 1, "+12345678901234", true).isDeliverable()); // 15 chars
        assertFalse(new SmsNotification("U", "M", 1, "+12345678", true).isDeliverable()); // 9 chars
        assertFalse(new SmsNotification("U", "M", 1, "+123456789012345", true).isDeliverable()); // 16 chars
        assertFalse(new SmsNotification("U", "M", 1, "380501234567", true).isDeliverable()); // no +
        assertFalse(new SmsNotification("U", "M", 1, null, true).isDeliverable()); // null phone

        sms.send();
        assertEquals(NotificationStatus.SENT, sms.getStatus());
    }

    @Test
    @DisplayName("Test PushNotification full flow and logic")
    void testPushNotification() throws NotDeliverableException {
        PushNotification push = new PushNotification("Mobile App User", "New message from Sarah", 3, VALID_TOKEN, "https://example.com/icons/msg.png");
        
        assertTrue(push.isDeliverable());
        assertFalse(push.isSilent());
        assertEquals("🔔 New message from Sarah", push.getFormattedMessage());
        assertEquals(1, push.estimateDeliverySeconds());

        // Test silent message
        PushNotification silent = new PushNotification("System User", "   ", 3, VALID_TOKEN, "https://example.com/icons/sync.png");
        assertTrue(silent.isSilent());
        assertEquals("🔔 (silent)", silent.getFormattedMessage());

        // Test deliverability bounds for deviceToken
        assertFalse(new PushNotification("U", "M", 1, "token-9ch", "url").isDeliverable()); // 9 chars
        assertFalse(new PushNotification("U", "M", 1, "token-10ch", "url").isDeliverable()); // 10 chars
        assertTrue(new PushNotification("U", "M", 1, "token-11ch-", "url").isDeliverable()); // 11 chars
        assertFalse(new PushNotification("U", "M", 1, "", "url").isDeliverable());
        assertFalse(new PushNotification("U", "M", 1, null, "url").isDeliverable());

        push.send();
        assertEquals(NotificationStatus.SENT, push.getStatus());
    }

    @Test
    @DisplayName("Test base validation in constructor")
    void testValidation() {
        // Recipient validation
        assertThrows(InvalidNotificationException.class, () -> new EmailNotification(null, "Msg", 1, VALID_SENDER, "Sub", false));
        assertThrows(InvalidNotificationException.class, () -> new EmailNotification("", "Msg", 1, VALID_SENDER, "Sub", false));
        assertThrows(InvalidNotificationException.class, () -> new EmailNotification("   ", "Msg", 1, VALID_SENDER, "Sub", false));

        // Message validation
        assertThrows(InvalidNotificationException.class, () -> new EmailNotification("Valid Recipient", null, 1, VALID_SENDER, "Sub", false));
        assertDoesNotThrow(() -> new EmailNotification("Valid Recipient", "", 1, VALID_SENDER, "Sub", false));

        // Priority validation
        assertThrows(InvalidNotificationException.class, () -> new EmailNotification("Valid Recipient", "Msg", 0, VALID_SENDER, "Sub", false));
        assertThrows(InvalidNotificationException.class, () -> new EmailNotification("Valid Recipient", "Msg", 6, VALID_SENDER, "Sub", false));
        assertDoesNotThrow(() -> new EmailNotification("Valid Recipient", "Msg", 1, VALID_SENDER, "Sub", false));
        assertDoesNotThrow(() -> new EmailNotification("Valid Recipient", "Msg", 5, VALID_SENDER, "Sub", false));
    }

    @Test
    @DisplayName("Test sorting by priority descending")
    void testSorting() {
        List<Notification> list = new ArrayList<>();
        list.add(new EmailNotification("low@example.com", "m", 1, VALID_SENDER, "Low", false));
        list.add(new EmailNotification("urgent@example.com", "m", 5, VALID_SENDER, "Urgent", false));
        list.add(new EmailNotification("medium@example.com", "m", 3, VALID_SENDER, "Medium", false));
        list.add(new EmailNotification("critical@example.com", "m", 5, VALID_SENDER, "Critical", false));

        Collections.sort(list);

        assertEquals(5, list.get(0).getPriority());
        assertEquals(5, list.get(1).getPriority());
        assertEquals(3, list.get(2).getPriority());
        assertEquals(1, list.get(3).getPriority());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Claim your FREE prize!",
            "Win big today!",
            "CLICK here to unlock offer",
            "This is a free gift for you",
            "Congratulations! You win a million"
    })
    @DisplayName("Test Email spam detection with various keywords")
    void testEmailSpamDetection(String subject) {
        EmailNotification email = new EmailNotification(VALID_EMAIL, "Message content", 1, VALID_SENDER, subject, false);
        assertTrue(email.isSpam(), "Subject '" + subject + "' should be detected as spam");
    }

    @Test
    @DisplayName("Test high priority logic")
    void testHighPriority() {
        assertFalse(new EmailNotification(VALID_EMAIL, "m", 1, VALID_SENDER, "t", false).isHighPriority());
        assertFalse(new EmailNotification(VALID_EMAIL, "m", 2, VALID_SENDER, "t", false).isHighPriority());
        assertFalse(new EmailNotification(VALID_EMAIL, "m", 3, VALID_SENDER, "t", false).isHighPriority());
        assertTrue(new EmailNotification(VALID_EMAIL, "m", 4, VALID_SENDER, "t", false).isHighPriority());
        assertTrue(new EmailNotification(VALID_EMAIL, "m", 5, VALID_SENDER, "t", false).isHighPriority());
    }
}
