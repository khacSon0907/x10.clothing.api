package x10.Clothing.api.service.authService;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.service.authService.registerUc.IRegisterUc;
import x10.Clothing.api.service.authService.registerUc.RegisterResponse;
import x10.Clothing.api.service.authService.verifyOtpUc.IVerifyOtpUc;
import x10.Clothing.api.service.authService.verifyOtpUc.VerifyOtpReq;
import x10.Clothing.api.service.authService.loginUc.ILoginUc;
import x10.Clothing.api.service.authService.loginUc.LoginReq;
import x10.Clothing.api.service.authService.loginUc.LoginResponse;
import x10.Clothing.api.service.authService.logoutUc.ILogoutUc;
import x10.Clothing.api.service.authService.refreshTokenUc.IRefreshTokenUc;
import x10.Clothing.api.config.jwt.JwtProperties;
import x10.Clothing.api.util.CookieUtil;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.user.UserError;
import x10.Clothing.api.service.userService.createUserUc.CreateUserReq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
@AllArgsConstructor
public class CoreAuthServiceImpl implements ICoreAuthService {

    private final IRegisterUc registerUc;
    private final IVerifyOtpUc verifyOtpUc;
    private final ILoginUc loginUc;
    private final ILogoutUc logoutUc;
    private final IRefreshTokenUc refreshTokenUc;
    private final JwtProperties jwtProperties;

    @Override
    public RegisterResponse register(CreateUserReq  req) {
        return registerUc.register(req);
    }

    @Override
    public void verifyOtp(VerifyOtpReq req) {
        verifyOtpUc.verifyOtp(req);
    }

    @Override
    public LoginResponse login(LoginReq req) {
        return loginUc.login(req);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        logoutUc.logout(request, response);
    }

    @Override
    public LoginResponse refreshToken(String bodyToken, HttpServletRequest request, HttpServletResponse response) {
        // 1. Cố gắng lấy refresh token từ Cookie
        String refreshToken = CookieUtil.getCookie(request, "refreshToken");

        // 2. Nếu không có ở Cookie, lấy từ request body làm phương án dự phòng (fallback)
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            refreshToken = bodyToken;
        }

        // 3. Nếu vẫn trống, ném lỗi không hợp lệ
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            throw new BusinessException(UserError.INVALID_REFRESH_TOKEN);
        }

        // 4. Thực thi Use Case làm mới token và nhận các token mới
        LoginResponse loginResponse = refreshTokenUc.refreshToken(refreshToken);

        // 5. Tính toán hạn dùng tối đa (max-age) bằng giây từ cấu hình Refresh Token Expiration (milis)
        long maxAgeSeconds = jwtProperties.getRefreshTokenExpiration() / 1000;

        // 6. Ghi đè Cookie refreshToken mới
        CookieUtil.addCookie(response, "refreshToken", loginResponse.getRefreshToken(), maxAgeSeconds);

        return loginResponse;
    }
}
