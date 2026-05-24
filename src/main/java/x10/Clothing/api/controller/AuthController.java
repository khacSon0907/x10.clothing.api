package x10.Clothing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import x10.Clothing.api.service.authService.ICoreAuthService;
import x10.Clothing.api.service.authService.changePasswordUc.ChangePasswordReq;
import x10.Clothing.api.service.authService.forgotPasswordUc.ForgotPasswordReq;
import x10.Clothing.api.service.authService.registerUc.RegisterResponse;
import x10.Clothing.api.service.authService.resetPasswordUc.ResetPasswordReq;
import x10.Clothing.api.service.authService.verifyForgotPasswordOtpUc.VerifyForgotPasswordOtpReq;
import x10.Clothing.api.service.authService.verifyForgotPasswordOtpUc.VerifyForgotPasswordOtpResponse;
import x10.Clothing.api.service.authService.verifyOtpUc.VerifyOtpReq;
import x10.Clothing.api.service.userService.createUserUc.CreateUserReq;
import x10.Clothing.api.share.response.ApiResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import x10.Clothing.api.service.authService.loginUc.LoginReq;
import x10.Clothing.api.service.authService.loginUc.LoginResponse;
import x10.Clothing.api.service.authService.refreshTokenUc.RefreshTokenReq;
import x10.Clothing.api.config.jwt.JwtProperties;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ICoreAuthService coreAuthService;
    private final JwtProperties jwtProperties;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<LoginResponse> login(
            @Valid @RequestBody LoginReq req,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        LoginResponse loginResponse = coreAuthService.login(req);

        // Calculate max-age in seconds from expiration in milliseconds
        long maxAgeSeconds = jwtProperties.getRefreshTokenExpiration() / 1000;

        x10.Clothing.api.util.CookieUtil.addCookie(response, "refreshToken", loginResponse.getRefreshToken(), maxAgeSeconds);

        return ApiResponse.success(
                200,
                "AUTH.LOGIN_SUCCESS",
                "Đăng nhập thành công",
                loginResponse,
                request.getRequestURI(),
                null
        );
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RegisterResponse> register(
            @Valid @RequestBody CreateUserReq req,
            HttpServletRequest request
    ) {
        RegisterResponse response = coreAuthService.register(req);
        return ApiResponse.success(
                201,
                "AUTH.REGISTER_SUCCESS",
                "Đăng ký thành công",
                response,
                request.getRequestURI(),
                null
        );
    }

    @PostMapping("/verify-otp")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> verifyOtp(
            @Valid @RequestBody VerifyOtpReq req,
            HttpServletRequest request
    ) {
        coreAuthService.verifyOtp(req);
        return ApiResponse.success(
                200,
                "AUTH.VERIFY_OTP_SUCCESS",
                "Xác thực email thành công",
                null,
                request.getRequestURI(),
                null
        );
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        coreAuthService.logout(request, response);
        return ApiResponse.success(
                200,
                "AUTH.LOGOUT_SUCCESS",
                "Đăng xuất thành công",
                null,
                request.getRequestURI(),
                null
        );
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<LoginResponse> refreshToken(
            @RequestBody(required = false) RefreshTokenReq req,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String bodyToken = (req != null) ? req.getRefreshToken() : null;
        LoginResponse loginResponse = coreAuthService.refreshToken(bodyToken, request, response);
        return ApiResponse.success(
                200,
                "AUTH.REFRESH_SUCCESS",
                "Làm mới token thành công",
                loginResponse,
                request.getRequestURI(),
                null
        );
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> forgotPassword(
            @Valid @RequestBody  ForgotPasswordReq req,
            HttpServletRequest request
    ) {
        coreAuthService.forgotPassword(req);
        return ApiResponse.success(
                200,
                "AUTH.FORGOT_PASSWORD_SUCCESS",
                "Mã xác nhận quên mật khẩu đã được gửi đến email của bạn",
                null,
                request.getRequestURI(),
                null
        );
    }

    @PostMapping("/verify-forgot-password-otp")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<VerifyForgotPasswordOtpResponse> verifyForgotPasswordOtp(
            @Valid @RequestBody VerifyForgotPasswordOtpReq req,
            HttpServletRequest request
    ) {
            VerifyForgotPasswordOtpResponse response = coreAuthService.verifyForgotPasswordOtp(req);
        return ApiResponse.success(
                200,
                "AUTH.VERIFY_FORGOT_PASSWORD_OTP_SUCCESS",
                "Xác thực mã OTP thành công",
                response,
                request.getRequestURI(),
                null
        );
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> resetPassword(
            @Valid @RequestBody ResetPasswordReq req,
            HttpServletRequest request
    ) {
        coreAuthService.resetPassword(req);
        return ApiResponse.success(
                200,
                "AUTH.RESET_PASSWORD_SUCCESS",
                "Đặt lại mật khẩu thành công",
                null,
                request.getRequestURI(),
                null
        );
    }

    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> changePassword(
            @Valid @RequestBody ChangePasswordReq req,
            HttpServletRequest request
    ) {
        // Extract userId from JWT token (stored in SecurityContext by JwtAuthenticationFilter)
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        coreAuthService.changePassword(userId, req);
        return ApiResponse.success(
                200,
                "AUTH.CHANGE_PASSWORD_SUCCESS",
                "Đổi mật khẩu thành công",
                null,
                request.getRequestURI(),
                null
        );
    }
}