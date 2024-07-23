package com.fc.toyproeject2.global.util;

import com.fc.toyproeject2.domain.register.model.request.EmailSendRequest;
import com.fc.toyproeject2.global.exception.error.AuthErrorCode;
import com.fc.toyproeject2.global.exception.type.AuthException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Properties;

@Configuration
public class EmailUtil {

    @Value("${email.id}")
    private String email;

    @Value("${email.pw}")
    private String pw;

    public void sendSingUpRandomNumberEmail(EmailSendRequest request, String successKey) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.naver.com");
        javaMailSender.setUsername(email);
        javaMailSender.setPassword(pw);

        javaMailSender.setPort(465);

        javaMailSender.setJavaMailProperties(getMailProperties());

        MimeMessage m = javaMailSender.createMimeMessage();
        MimeMessageHelper h = new MimeMessageHelper(m, "UTF-8");

        try {
            h.setFrom(email+"@naver.com");
            h.setTo(request.email());
            h.setSubject("인증번호 발송");
            h.setText("인증번호 : " + successKey);
        } catch (Exception e) {
            throw new AuthException(AuthErrorCode.ERROR_SEND_EMAIL);
        }
        javaMailSender.send(m);
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.debug", "false");
        properties.setProperty("mail.smtp.ssl.trust", "smtp.naver.com");
        properties.setProperty("mail.smtp.ssl.enable", "true");
        return properties;
    }

}
