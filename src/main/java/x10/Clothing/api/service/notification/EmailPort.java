package x10.Clothing.api.service.notification;

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
}