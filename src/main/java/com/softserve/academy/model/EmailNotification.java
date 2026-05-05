package com.softserve.academy.model;

import com.softserve.academy.exception.NotDeliverableException;

import java.util.List;

public class EmailNotification extends Notification {
    private String senderEmail;
    private String subject;
    private boolean hasAttachment;

    public EmailNotification(String recipient, String message, int priority, String senderEmail, String subject, boolean hasAttachment) {
        super(recipient, message, priority);
        this.senderEmail = senderEmail;
        this.subject = subject;
        this.hasAttachment = hasAttachment;
    }

    @Override
    public boolean isDeliverable() {
        if (getRecipient() == null || getRecipient().isBlank()) {
            return false;
        }
        String email = getRecipient();
        return email.contains("@") && 
            email.contains(".") && 
            email.indexOf("@") < email.lastIndexOf(".");
    }

    public boolean isSpam() {
        if (subject == null) return false;

        String lower = subject.toLowerCase();
        return lower.contains("free") || lower.contains("win") || lower.contains("click");
    }

    @Override
    public String getFormattedMessage() {
        String subjectText = (subject != null) ? subject : "[No Subject]";
        return "Subject: " + subjectText + "\n" + getMessage();
    }

    @Override
    public int estimateDeliverySeconds() {
        return 30;
    }

    @Override
    protected void performSend() throws NotDeliverableException {
        if (isSpam()) {
            throw new NotDeliverableException("Email is detected as spam");
        }

        System.out.println("Sending EMAIL to " + recipient + ": " + getFormattedMessage());
    }

    public String getSenderEmail() { return senderEmail; }
    public String getSubject() { return subject; }
    public boolean isHasAttachment() { return hasAttachment; }
}
