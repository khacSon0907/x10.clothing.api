package x10.Clothing.api.service.authService.loginUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IUserRepository;
import x10.Clothing.api.common.domain.dto.request.TokenPayload;
import x10.Clothing.api.common.domain.entities.UserEntity;
import x10.Clothing.api.common.domain.enums.UserStatus;
import x10.Clothing.api.config.jwt.IJwtService;
import x10.Clothing.api.config.redis.IRedisService;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.user.UserError;

import java.time.Duration;

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
        String email = req.getEmail();

        // 1. Check if user is locked due to too many failed attempts
        if (redisService.isLoginLocked(email)) {
            throw new BusinessException(UserError.LOGIN_LOCKED);
        }

        // 2. Find user by email
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        // 3. Verify password
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            long failedCount = redisService.incrementFailedLogin(email);
            log.warn("Failed login attempt {} for user {}", failedCount, email);

            if (failedCount >= MAX_FAILED_ATTEMPTS) {
                redisService.lockUserLogin(email, LOCK_DURATION);
                redisService.resetFailedLogin(email);
                throw new BusinessException(UserError.LOGIN_LOCKED);
            }

            throw new BusinessException(UserError.INVALID_CREDENTIALS);
        }

        // Password is correct, reset failed attempts
        redisService.resetFailedLogin(email);

        // 4. Check if email is verified
        if (!user.isEmailVerified()) {
            throw new BusinessException(UserError.EMAIL_NOT_VERIFIED);
        }

        // 5. Check if user is active
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException(UserError.USER_NOT_ACTIVE);
        }

        // 6. Generate Tokens
        TokenPayload payload = TokenPayload.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role("USER") // Default role, modify as needed based on your entity
                .build();

        String accessToken = jwtService.generateAccessToken(payload);
        String refreshToken = jwtService.generateRefreshToken(payload);

        // 7. Return LoginResponse
        return LoginResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
