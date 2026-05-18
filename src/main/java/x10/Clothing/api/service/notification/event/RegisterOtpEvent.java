package x10.Clothing.api.service.notification.event;

public record RegisterOtpEvent(
        String email,
        String username,
        String otp
) {
}