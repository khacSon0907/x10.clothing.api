package x10.Clothing.api.service.authService;

import org.springframework.security.oauth2.core.user.OAuth2User;
import x10.Clothing.api.service.authService.changePasswordUc.ChangePasswordReq;
import x10.Clothing.api.service.authService.forgotPasswordUc.ForgotPasswordReq;
import x10.Clothing.api.service.authService.registerUc.RegisterResponse;
import x10.Clothing.api.service.authService.resetPasswordUc.ResetPasswordReq;
import x10.Clothing.api.service.authService.verifyForgotPasswordOtpUc.VerifyForgotPasswordOtpReq;
import x10.Clothing.api.service.authService.verifyForgotPasswordOtpUc.VerifyForgotPasswordOtpResponse;
import x10.Clothing.api.service.userService.createUserUc.CreateUserReq;
import x10.Clothing.api.service.authService.verifyOtpUc.VerifyOtpReq;
import x10.Clothing.api.service.authService.loginUc.LoginReq;
import x10.Clothing.api.service.authService.loginUc.LoginResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ICoreAuthService {
    RegisterResponse register(CreateUserReq req);
    void verifyOtp(VerifyOtpReq req);
    LoginResponse login(LoginReq req);
    void logout(HttpServletRequest request, HttpServletResponse response);
    LoginResponse refreshToken(String bodyToken, HttpServletRequest request, HttpServletResponse response);
    void forgotPassword(ForgotPasswordReq req);

    VerifyForgotPasswordOtpResponse verifyForgotPasswordOtp(VerifyForgotPasswordOtpReq req);

    void resetPassword(ResetPasswordReq req);

    void changePassword(String userId, ChangePasswordReq req);

        LoginResponse loginWithGoogle(OAuth2User req);
}
