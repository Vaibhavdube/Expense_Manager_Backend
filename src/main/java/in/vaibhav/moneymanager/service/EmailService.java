package in.vaibhav.moneymanager.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
    @RequiredArgsConstructor
    public class EmailService {

    //dependency used
    private final JavaMailSender mailSender;

    @Value("${MAIL_USERNAME}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(fromEmail);

            message.setTo(to);

            message.setSubject(subject);

            message.setText(body);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

//From: yourapp@gmail.com
// To: abc@gmail.com
// Subject: Verify Account
//
// Click here to verify:
// http://localhost:8080/verify?token=abc123
//
//
//     //now call this service method in ProfileService(parent service )service

