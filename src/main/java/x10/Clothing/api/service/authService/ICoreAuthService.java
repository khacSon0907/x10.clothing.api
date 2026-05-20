package x10.Clothing.api.service.authService;

import x10.Clothing.api.service.authService.registerUc.RegisterResponse;
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
}
