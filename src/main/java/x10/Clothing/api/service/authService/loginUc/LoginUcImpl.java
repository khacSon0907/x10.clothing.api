package x10.Clothing.api.service.authService.loginUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IUserRepository;
import x10.Clothing.api.common.domain.dto.request.TokenPayload;
import x10.Clothing.api.common.domain.entities.UserEntity;
import x10.Clothing.api.common.domain.enums.AuthProvider;
import x10.Clothing.api.common.domain.enums.UserRole;
import x10.Clothing.api.common.domain.enums.UserStatus;
import x10.Clothing.api.config.jwt.IJwtService;
import x10.Clothing.api.config.redis.IRedisService;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.user.UserError;

import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginUcImpl implements ILoginUc {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRedisService redisService;
    private final IJwtService jwtService;

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(5);

    @Override
    public LoginResponse login(LoginReq req) {

        String email = req.getEmail().trim().toLowerCase();

        // 1. Check login lock
        if (redisService.isLoginLocked(email)) {
            throw new BusinessException(UserError.LOGIN_LOCKED);
        }

        // 2. Find user
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));



        if (user.getProviderType() == AuthProvider.GOOGLE
                && (user.getPasswordHash() == null || user.getPasswordHash().isBlank())) {

            throw new BusinessException(UserError.GOOGLE_ACCOUNT_LOGIN_REQUIRED);
        }

        if (user.getProviderType() == AuthProvider.GUEST) {
            throw new BusinessException(UserError.GUEST_ACCOUNT_CANNOT_LOGIN);
        }

        // 4. Verify password
        if (user.getPasswordHash() == null
                || user.getPasswordHash().isBlank()
                || !passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {

            long failedCount = redisService.incrementFailedLogin(email);

            log.warn("Failed login attempt {} for user {}", failedCount, email);

            if (failedCount >= MAX_FAILED_ATTEMPTS) {

                redisService.lockUserLogin(email, LOCK_DURATION);
                redisService.resetFailedLogin(email);

                throw new BusinessException(UserError.LOGIN_LOCKED);
            }

            throw new BusinessException(UserError.INVALID_CREDENTIALS);
        }

        // 5. Reset failed login
        redisService.resetFailedLogin(email);

        // 6. Check email verified
        if (!user.isEmailVerified()) {
            throw new BusinessException(UserError.EMAIL_NOT_VERIFIED);
        }

        // 7. Check user status
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException(UserError.USER_NOT_ACTIVE);
        }

        // 8. Build roles
        Set<UserRole> roles = user.getRoles();

        if (roles == null || roles.isEmpty()) {
            roles = Set.of(UserRole.USER);
        }

        String roleStr = roles.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));

        // 9. Build token payload
        TokenPayload payload = TokenPayload.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(roleStr)
                .build();

        // 10. Generate tokens
        String accessToken = jwtService.generateAccessToken(payload);
        String refreshToken = jwtService.generateRefreshToken(payload);

        // 11. Return response
        return LoginResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}