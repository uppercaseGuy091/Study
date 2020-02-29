package com.lab1.study;

import android.util.Log;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Mail {

    private final String MY_EMAIL = "studycards05@gmail.com";
    private final String MY_PASSWORD = "studycards05@5";

        private String toEmail;
        private String emailSubject;
        private String emailText;

    public Mail(String toEmail, String emailSubject, String emailText) {
        this.toEmail = toEmail;
        this.emailSubject = emailSubject;
        this.emailText = emailText;
    }

     public void send() throws RuntimeException{
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(MY_EMAIL, MY_PASSWORD);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MY_EMAIL));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));
            message.setSubject(emailSubject);
            message.setText(emailText);

            Transport.send(message);

        } catch (MessagingException e) {
            System.out.println(e.toString());
            Log.i("Mail Exception",e.toString());
            throw new RuntimeException(e);

        }
    }


}




