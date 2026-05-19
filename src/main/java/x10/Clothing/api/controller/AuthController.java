package x10.Clothing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import x10.Clothing.api.service.authService.ICoreAuthService;
import x10.Clothing.api.service.authService.registerUc.RegisterResponse;
import x10.Clothing.api.service.authService.verifyOtpUc.VerifyOtpReq;
import x10.Clothing.api.service.userService.createUserUc.CreateUserReq;
import x10.Clothing.api.share.response.ApiResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import x10.Clothing.api.service.authService.loginUc.LoginReq;
import x10.Clothing.api.service.authService.loginUc.LoginResponse;
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
}