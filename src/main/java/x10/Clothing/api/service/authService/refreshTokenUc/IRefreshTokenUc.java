package x10.Clothing.api.service.authService.refreshTokenUc;

import x10.Clothing.api.service.authService.loginUc.LoginResponse;

public interface IRefreshTokenUc {
    LoginResponse refreshToken(String refreshToken);
}
