package x10.Clothing.api.service.notification;

import x10.Clothing.api.common.domain.entities.OrderEntity;

public interface EmailPort {

    void sendRegisterOtpEmail(
            String to,
            String username,
            String otp
    );

    void sendForgotPasswordOtpEmail(
            String to,
            String username,
            String otp
    );

    void sendOrderInvoiceEmail(
            String to,
            String username,
            OrderEntity order
    );
}
