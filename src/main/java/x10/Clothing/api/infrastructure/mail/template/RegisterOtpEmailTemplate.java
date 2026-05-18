package x10.Clothing.api.infrastructure.mail.template;

public class RegisterOtpEmailTemplate {

    private RegisterOtpEmailTemplate() {
    }

    public static String build(
            String username,
            String otp
    ) {
        return """
                Hello %s,

                Your X10 Clothing verification code is:

                %s

                This code will expire in 5 minutes.

                If you did not create this account, please ignore this email.

                X10 Clothing Store
                """.formatted(username, otp);
    }
}