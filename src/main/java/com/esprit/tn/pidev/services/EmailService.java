package com.esprit.tn.pidev.services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private static final String FROM_EMAIL = "haifacheikh27@gmail.com";
    private static final String PASSWORD = "qssp tzaa emqo pkef";


    public static void sendEmail(String toEmail, String subject, String body, boolean isHtml) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);

        // Définir le contenu en fonction du type (HTML ou texte brut)
        if (isHtml) {
            message.setContent(body, "text/html");
        } else {
            message.setText(body);
        }

        Transport.send(message);
    }


    public static void sendEmail(String toEmail, String subject, String body) throws MessagingException {
        sendEmail(toEmail, subject, body, false);
    }


    public static void sendHtmlEmail(String toEmail, String subject, String htmlBody) throws MessagingException {
        sendEmail(toEmail, subject, htmlBody, true);
    }
}