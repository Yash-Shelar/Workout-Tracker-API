package fit.workout_tracker.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import fit.workout_tracker.api.entity.VerificationToken;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    private static final String subject = "Account Verification";

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;

    @Value("${application.domain}")
    private String domain;

    @Value("${application.protocol}")
    private String protocol;

    public EmailService(
        JavaMailSender javaMailSender,
        SpringTemplateEngine springTemplateEngine
    ) {
        this.javaMailSender = javaMailSender;
        this.springTemplateEngine = springTemplateEngine;
    }

    public void sendEmail(VerificationToken token) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(token.getUser().getUsername());
            helper.setSubject(subject);

            String finalHtml = generateHtml(token.getToken().toString());

            helper.setText(finalHtml, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateHtml(String token) {
        Context context = new Context();
        context.setVariable("token", token);
        context.setVariable("protocol", protocol);
        context.setVariable("domain", domain);

        return springTemplateEngine.process("/verify", context);
    }
}
