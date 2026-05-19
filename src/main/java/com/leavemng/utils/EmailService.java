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
        String usernameEnv = "gapaarfr@gmail.com";
        String passwordEnv = "grsyxkjgbsqacylj";
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
        message.setContent(buildMessageBody(username, leaveRequest, decision), "text/html; charset=UTF-8");

        Transport.send(message);
    }

    private static String buildMessageBody(String username, LeaveRequest leaveRequest, String decision) {
        String safeUsername = escapeHtml(username);
        String safeDecision = escapeHtml(decision);
        String safeStartDate = escapeHtml(String.valueOf(leaveRequest.getStartDate()));
        String safeEndDate = escapeHtml(String.valueOf(leaveRequest.getEndDate()));
        String safeReason = escapeHtml(leaveRequest.getReason());

        return "<!DOCTYPE html>"
            + "<html lang=\"en\">"
            + "<head>"
            + "<meta charset=\"UTF-8\">"
            + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
            + "</head>"
            + "<body style=\"margin:0;padding:0;background:#f3f6fb;font-family:Arial,Helvetica,sans-serif;color:#1f2937;\">"
            + "<div style=\"max-width:640px;margin:0 auto;padding:32px 16px;\">"
            + "<div style=\"background:linear-gradient(135deg,#0f172a 0%,#1d4ed8 100%);border-radius:18px 18px 0 0;padding:28px 32px;color:#ffffff;\">"
            + "<div style=\"font-size:13px;letter-spacing:1.5px;text-transform:uppercase;opacity:.85;\">Leave Management System</div>"
            + "<h1 style=\"margin:12px 0 0;font-size:28px;line-height:1.2;\">Leave request decision</h1>"
            + "</div>"
            + "<div style=\"background:#ffffff;border-radius:0 0 18px 18px;padding:32px;box-shadow:0 10px 30px rgba(15,23,42,.12);\">"
            + "<p style=\"margin:0 0 18px;font-size:16px;line-height:1.7;\">Hello <strong>" + safeUsername + "</strong>,</p>"
            + "<p style=\"margin:0 0 24px;font-size:16px;line-height:1.7;\">Your leave request has been <span style=\"display:inline-block;padding:4px 10px;border-radius:999px;background:#dcfce7;color:#166534;font-weight:700;text-transform:uppercase;font-size:12px;letter-spacing:.5px;\">" + safeDecision + "</span>.</p>"
            + "<div style=\"border:1px solid #e5e7eb;border-radius:14px;overflow:hidden;margin:24px 0;\">"
            + "<div style=\"background:#f8fafc;padding:14px 18px;font-weight:700;color:#0f172a;\">Request details</div>"
            + "<table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"border-collapse:collapse;\">"
            + detailRow("Start date", safeStartDate)
            + detailRow("End date", safeEndDate)
            + detailRow("Reason", safeReason)
            + "</table>"
            + "</div>"
            + "<p style=\"margin:28px 0 0;font-size:15px;line-height:1.7;color:#475569;\">If you have any questions, please contact your HR or administrator team.</p>"
            + "<p style=\"margin:18px 0 0;font-size:15px;line-height:1.7;\">Best regards,<br><strong>Leave Management System</strong></p>"
            + "</div>"
            + "</div>"
            + "</body>"
            + "</html>";
    }

    private static String detailRow(String label, String value) {
        return "<tr>"
            + "<td style=\"padding:14px 18px;width:34%;background:#fbfdff;border-top:1px solid #e5e7eb;font-weight:700;color:#334155;vertical-align:top;\">" + label + "</td>"
            + "<td style=\"padding:14px 18px;border-top:1px solid #e5e7eb;color:#0f172a;vertical-align:top;\">" + value + "</td>"
            + "</tr>";
    }

    private static String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
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