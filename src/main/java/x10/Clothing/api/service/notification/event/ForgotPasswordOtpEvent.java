package x10.Clothing.api.service.notification.event;

public record ForgotPasswordOtpEvent(
        String email,
        String username,
        String otp
) {
}
