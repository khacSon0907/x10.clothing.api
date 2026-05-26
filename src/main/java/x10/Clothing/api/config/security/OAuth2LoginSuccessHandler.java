package x10.Clothing.api.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import x10.Clothing.api.service.authService.loginUc.LoginResponse;
import x10.Clothing.api.service.authService.oauth2LoginUc.GoogleOauth2Service;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final GoogleOauth2Service googleOauth2Service;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        LoginResponse loginResponse = googleOauth2Service.loginWithGoogle(oauth2User);

        String redirectUrl = UriComponentsBuilder
                .fromUriString(frontendUrl + "/oauth2/success")
                .queryParam("accessToken", loginResponse.getAccessToken())
                .queryParam("refreshToken", loginResponse.getRefreshToken())
                .build()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}