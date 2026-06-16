package x10.Clothing.api.infrastructure.mail.smtp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import x10.Clothing.api.common.domain.entities.OrderEntity;
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
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mailFrom);
        message.setTo(to);
        message.setSubject("X10 Clothing invoice - " + order.getOrderCode());
        message.setText(
                OrderInvoiceEmailTemplate.build(username, order)
        );

        javaMailSender.send(message);
    }
}
