package com.leavemng.utils;

import com.leavemng.models.LeaveRequest;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public final class EmailService {

    private EmailService() {
    }

    public static void sendLeaveDecisionEmail(String toEmail, String username, LeaveRequest leaveRequest, String decision) throws Exception {
        String host = getConfig("SMTP_HOST", "smtp.gmail.com");
        String port = getConfig("SMTP_PORT", "587");
        String usernameEnv = getRequiredConfig("SMTP_USERNAME");
        String passwordEnv = getRequiredConfig("SMTP_PASSWORD");
        String fromEmail = getConfig("SMTP_FROM", usernameEnv);

        if (usernameEnv.isBlank() || passwordEnv.isBlank()) {
            throw new IllegalStateException("SMTP is not configured. Set SMTP_USERNAME and SMTP_PASSWORD.");
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usernameEnv, passwordEnv);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Leave request " + decision);
        message.setText(buildMessageBody(username, leaveRequest, decision));

        Transport.send(message);
    }

    private static String buildMessageBody(String username, LeaveRequest leaveRequest, String decision) {
        return "Hello " + username + ",\n\n"
            + "Your leave request has been " + decision + ".\n\n"
            + "Request details:\n"
            + "- Start date: " + leaveRequest.getStartDate() + "\n"
            + "- End date: " + leaveRequest.getEndDate() + "\n"
            + "- Reason: " + leaveRequest.getReason() + "\n\n"
            + "Best regards,\n"
            + "Leave Management System";
    }

    private static String getConfig(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            value = System.getenv(key);
        }
        return value == null || value.isBlank() ? defaultValue : value;
    }

    private static String getRequiredConfig(String key) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            value = System.getenv(key);
        }
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("SMTP is not configured. Set " + key + ".");
        }
        return value;
    }
}