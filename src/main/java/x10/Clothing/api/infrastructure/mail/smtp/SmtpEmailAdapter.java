package x10.Clothing.api.infrastructure.mail.smtp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import x10.Clothing.api.infrastructure.mail.template.RegisterOtpEmailTemplate;
import x10.Clothing.api.service.notification.EmailPort;

@Component
@RequiredArgsConstructor
public class SmtpEmailAdapter implements EmailPort {

    private final JavaMailSender javaMailSender;

    @Value("${app.mail.from}")
    private String mailFrom;

    @Override
    public void sendRegisterOtpEmail(
            String to,
            String username,
            String otp
    ) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mailFrom);
        message.setTo(to);
        message.setSubject("Your X10 Clothing verification code");
        message.setText(
                RegisterOtpEmailTemplate.build(username, otp)
        );

        javaMailSender.send(message);
    }
}