package com.example.webshop.services.impl;

import com.example.webshop.services.MailService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;
    private static final String SUBJECT = "WebShop - Verification";
    private static final String TEXT = "Welcome to WebShop application.";

    @Value("${spring.mail.username}")
    private String from;

    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendMail(String mailTo, String pin) {
        String text = TEXT + "\nPIN: " + pin;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(mailTo);
        mailMessage.setSubject(SUBJECT);
        mailMessage.setText(text);
        javaMailSender.send(mailMessage);
        Logger.getLogger(getClass()).info("SEND MAIL to " + " " + mailTo);
    }
}