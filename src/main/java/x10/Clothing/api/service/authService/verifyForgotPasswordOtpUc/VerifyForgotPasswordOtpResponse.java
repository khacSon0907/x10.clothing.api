package x10.Clothing.api.service.authService.verifyForgotPasswordOtpUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyForgotPasswordOtpResponse {
    private String resetToken;
}
