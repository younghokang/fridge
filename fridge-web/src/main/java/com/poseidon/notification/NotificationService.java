package com.poseidon.notification;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    
    private static final String EMAIL_FORGOT_PASSWORD_VERIFICATION_TEMPLATE_NAME = "mail/forgotPassword";
    
    @Async
    public void sendNotificationForgotPasswordVerification(String from, String to, Context context) throws MessagingException {
        log.info("Sending Email...");
        
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setTo(to);
        message.setFrom(from);
        message.setSubject("Fridge Account Verification");
        
        templateEngine.getTemplateResolvers().forEach(resolver -> {
            log.info("name: " + resolver.getName() + ", order: " + resolver.getOrder());
        });
        
        final String htmlContent = templateEngine.process(EMAIL_FORGOT_PASSWORD_VERIFICATION_TEMPLATE_NAME, context);
        message.setText(htmlContent, true);
        
        javaMailSender.send(mimeMessage);
        
        log.info("Email sent.");
    }

}
