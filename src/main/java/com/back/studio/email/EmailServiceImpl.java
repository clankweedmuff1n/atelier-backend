package com.back.studio.email;

import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl {

    @Autowired
    private JavaMailSender emailSender;

    //@PostConstruct
    private void init() {
        sendSimpleMessage("danilfpronkin280504@gmail.com", "Test", "TEST");
    }

    @SneakyThrows
    public void sendSimpleMessage(
            String to, String subject, String text) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(text, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom("noreply@engelrealestate.us");
        emailSender.send(mimeMessage);
    }
}
