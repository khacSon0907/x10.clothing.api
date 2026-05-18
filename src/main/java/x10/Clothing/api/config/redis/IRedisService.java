package x10.Clothing.api.config.redis;

// IRedisService.java

import java.time.Duration;

public interface IRedisService {

    // Lưu refresh token
    void saveRefreshToken(String userId, String refreshToken, Duration expiration);
    String getRefreshToken(String userId);
    void deleteRefreshToken(String userId);

    // Blacklist access token khi logout
    void blacklistToken(String token, Duration expiration);
    Boolean isTokenBlacklisted(String token);

    // Login attempts (rate limit)
    Integer getLoginAttempts(String username);
    Long incrementLoginAttempts(String username);
    void resetLoginAttempts(String username);
    void setLoginAttemptsExpire(String username, Duration duration);
}