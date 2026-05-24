package x10.Clothing.api.infrastructure.mail.template;

public class ForgotPasswordEmailTemplate {
    public static String build(String username, String otp) {
        return "Hello " + username + ",\n\n"
                + "You have requested to reset your password.\n"
                + "Your OTP for password reset is: " + otp + "\n\n"
                + "If you did not request this, please ignore this email.\n\n"
                + "Regards,\n"
                + "X10 Clothing Team";
    }
}
