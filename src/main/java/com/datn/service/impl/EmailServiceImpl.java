package com.datn.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl {
    @Autowired
    private JavaMailSender mailSender;
    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);// co the kem them file html hoac design mail gui di
        mimeMessageHelper.setTo(to); // send toi dia chi mail nao
        mimeMessageHelper.setSubject(subject);// kem subject la gi
        mimeMessageHelper.setText(body, true);// set text cho phep thiet ke mail bang html
        mailSender.send(mimeMessage);
    }
}
