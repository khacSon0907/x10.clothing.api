package x10.Clothing.api.config.redis;

// RedisServiceImpl.java

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisServiceImpl implements IRedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String LOGIN_ATTEMPT_PREFIX = "login_attempt:";

    @Override
    public void saveRefreshToken(String userId, String refreshToken, Duration expiration) {
        try {
            redisTemplate.opsForValue().set("refresh:" + userId, refreshToken, expiration);
        } catch (Exception e) {
            log.error("Error saving refresh token for user: {}", userId, e);
        }
    }

    @Override
    public String getRefreshToken(String userId) {
        try {
            return redisTemplate.opsForValue().get("refresh:" + userId);
        } catch (Exception e) {
            log.error("Error getting refresh token for user: {}", userId, e);
            return null;
        }
    }

    @Override
    public void deleteRefreshToken(String userId) {
        try {
            redisTemplate.delete("refresh:" + userId);
        } catch (Exception e) {
            log.error("Error deleting refresh token for user: {}", userId, e);
        }
    }

    @Override
    public void blacklistToken(String token, Duration expiration) {
        try {
            redisTemplate.opsForValue().set("blacklist:" + token, "1", expiration);
        } catch (Exception e) {
            log.error("Error blacklisting token", e);
        }
    }

    @Override
    public Boolean isTokenBlacklisted(String token) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token));
        } catch (Exception e) {
            log.error("Error checking blacklisted token", e);
            return false;
        }
    }

    @Override
    public Integer getLoginAttempts(String username) {
        try {
            String value = redisTemplate.opsForValue().get(LOGIN_ATTEMPT_PREFIX + username);
            return value != null ? Integer.parseInt(value) : 0;
        } catch (Exception e) {
            log.error("Error getting login attempts for user: {}", username, e);
            return 0;
        }
    }

    @Override
    public Long incrementLoginAttempts(String username) {
        try {
            return redisTemplate.opsForValue().increment(LOGIN_ATTEMPT_PREFIX + username);
        } catch (Exception e) {
            log.error("Error incrementing login attempts for user: {}", username, e);
            return 0L;
        }
    }

    @Override
    public void resetLoginAttempts(String username) {
        try {
            redisTemplate.delete(LOGIN_ATTEMPT_PREFIX + username);
        } catch (Exception e) {
            log.error("Error resetting login attempts for user: {}", username, e);
        }
    }

    @Override
    public void setLoginAttemptsExpire(String username, Duration duration) {
        try {
            redisTemplate.expire(LOGIN_ATTEMPT_PREFIX + username, duration);
        } catch (Exception e) {
            log.error("Error setting login attempts expiration for user: {}", username, e);
        }
    }
}