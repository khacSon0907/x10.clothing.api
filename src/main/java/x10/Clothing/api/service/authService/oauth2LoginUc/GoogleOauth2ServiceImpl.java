package x10.Clothing.api.service.authService.oauth2LoginUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IUserRepository;
import x10.Clothing.api.common.domain.dto.request.TokenPayload;
import x10.Clothing.api.common.domain.entities.UserEntity;
import x10.Clothing.api.common.domain.enums.AuthProvider;
import x10.Clothing.api.common.domain.enums.UserRole;
import x10.Clothing.api.common.domain.enums.UserStatus;
import x10.Clothing.api.config.jwt.IJwtService;
import x10.Clothing.api.service.authService.loginUc.LoginResponse;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.user.UserError;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleOauth2ServiceImpl implements GoogleOauth2Service {

    private final IUserRepository userRepository;
    private final IJwtService jwtService;

    @Override
    public LoginResponse loginWithGoogle(OAuth2User oauth2User) {

        log.info("Starting Google OAuth2 login process from OAuth2User");

        GoogleUserInfo googleUserInfo = extractGoogleUserInfo(oauth2User);

        log.info("Google user info extracted successfully for email: {}", googleUserInfo.getEmail());

        UserEntity user = userRepository.findByEmail(googleUserInfo.getEmail())
                .orElseGet(() -> {
                    log.info("User not found, creating new Google user with email: {}", googleUserInfo.getEmail());
                    return createNewGoogleUser(googleUserInfo);
                });

        if (!user.isEmailVerified()) {
            user.setEmailVerified(true);
            user.setVerifiedAt(LocalDateTime.now());
        }

        if (user.getProviderType() == null) {
            user.setProviderType(AuthProvider.GOOGLE);
        }

        if (user.getAvatarUrl() == null || user.getAvatarUrl().isBlank()) {
            user.setAvatarUrl(googleUserInfo.getPicture());
        }

        user.setUpdatedAt(Instant.now());
        user = userRepository.save(user);

        String roleString = user.getRoles() != null && !user.getRoles().isEmpty()
                ? user.getRoles().iterator().next().name()
                : UserRole.USER.name();

        TokenPayload tokenPayload = TokenPayload.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(roleString)
                .build();

        String accessToken = jwtService.generateAccessToken(tokenPayload);
        String refreshToken = jwtService.generateRefreshToken(tokenPayload);

        log.info("Google login success. Tokens generated for user: {}", user.getId());

        return LoginResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private GoogleUserInfo extractGoogleUserInfo(OAuth2User oauth2User) {

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");
        String googleId = oauth2User.getAttribute("sub");
        Boolean emailVerified = oauth2User.getAttribute("email_verified");

        if (email == null || email.isBlank()) {
            log.error("Google OAuth2 email is missing");
            throw new BusinessException(UserError.INVALID_GOOGLE_TOKEN);
        }

        if (emailVerified == null || !emailVerified) {
            log.warn("Google email is not verified: {}", email);
            throw new BusinessException(UserError.EMAIL_NOT_VERIFIED_GOOGLE);
        }

        return GoogleUserInfo.builder()
                .email(email)
                .name(name)
                .picture(picture)
                .googleId(googleId)
                .emailVerified(emailVerified)
                .build();
    }

    private UserEntity createNewGoogleUser(GoogleUserInfo googleUserInfo) {

        String userId = UUID.randomUUID().toString();
        String username = generateUsernameFromEmail(googleUserInfo.getEmail());

        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.USER);

        UserEntity newUser = UserEntity.builder()
                .id(userId)
                .username(username)
                .email(googleUserInfo.getEmail())
                .passwordHash(null)
                .status(UserStatus.ACTIVE)
                .emailVerified(true)
                .verifiedAt(LocalDateTime.now())
                .avatarUrl(googleUserInfo.getPicture())
                .providerType(AuthProvider.GOOGLE)
                .roles(roles)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        UserEntity savedUser = userRepository.save(newUser);

        log.info("New Google user created with id: {}", savedUser.getId());

        return savedUser;
    }

    private String generateUsernameFromEmail(String email) {
        return email.split("@")[0];
    }
}