package x10.Clothing.api.infrastructure.mail.smtp;

import lombok.RequiredArgsConstructor;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import x10.Clothing.api.common.domain.entities.order.OrderEntity;
import x10.Clothing.api.infrastructure.mail.template.OrderInvoiceEmailTemplate;
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

    @Override
    public void sendForgotPasswordOtpEmail(
            String to,
            String username,
            String otp
    ) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mailFrom);
        message.setTo(to);
        message.setSubject("Your X10 Clothing password reset code");
        message.setText(
                x10.Clothing.api.infrastructure.mail.template.ForgotPasswordEmailTemplate.build(username, otp)
        );

        javaMailSender.send(message);
    }

    @Override
    public void sendOrderInvoiceEmail(
            String to,
            String username,
            OrderEntity order
    ) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            helper.setFrom(mailFrom);
            helper.setTo(to);
            helper.setSubject("X10 Clothing invoice - " + order.getOrderCode());
            helper.setText(
                    OrderInvoiceEmailTemplate.build(username, order),
                    true
            );

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new MailPreparationException("Failed to prepare order invoice email", e);
        }
    }
}
