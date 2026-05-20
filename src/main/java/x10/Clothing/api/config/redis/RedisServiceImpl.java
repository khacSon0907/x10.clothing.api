package x10.Clothing.api.config.redis;

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

    private static final String REGISTER_OTP_PREFIX = "register_otp:";

    @Override
    public void saveRegisterOtp(
            String email,
            String otp,
            Duration expiration
    ) {
        try {
            String key = REGISTER_OTP_PREFIX + email;

            redisTemplate.opsForValue().set(
                    key,
                    otp,
                    expiration
            );

            log.info("OTP saved to Redis: {}", key);

        } catch (Exception e) {
            log.error(
                    "Error saving register OTP for email: {}",
                    email,
                    e
            );
        }
    }

    @Override
    public String getRegisterOtp(String email) {
        try {
            return redisTemplate.opsForValue().get(
                    REGISTER_OTP_PREFIX + email
            );
        } catch (Exception e) {
            log.error(
                    "Error getting register OTP for email: {}",
                    email,
                    e
            );
            return null;
        }
    }

    @Override
    public void deleteRegisterOtp(String email) {
        try {
            redisTemplate.delete(
                    REGISTER_OTP_PREFIX + email
            );
        } catch (Exception e) {
            log.error(
                    "Error deleting register OTP for email: {}",
                    email,
                    e
            );
        }
    }

    private static final String LOGIN_FAILED_PREFIX = "login_failed:";
    private static final String LOGIN_LOCKED_PREFIX = "login_locked:";

    @Override
    public long incrementFailedLogin(String email) {
        try {
            String key = LOGIN_FAILED_PREFIX + email;
            Long count = redisTemplate.opsForValue().increment(key);
            if (count != null && count == 1) {
                // Set expiration for 1 hour so the counter resets eventually
                redisTemplate.expire(key, Duration.ofHours(1));
            }
            return count != null ? count : 0;
        } catch (Exception e) {
            log.error("Error incrementing failed login for email: {}", email, e);
            return 0;
        }
    }

    @Override
    public void resetFailedLogin(String email) {
        try {
            redisTemplate.delete(LOGIN_FAILED_PREFIX + email);
        } catch (Exception e) {
            log.error("Error resetting failed login for email: {}", email, e);
        }
    }

    @Override
    public void lockUserLogin(String email, Duration duration) {
        try {
            String key = LOGIN_LOCKED_PREFIX + email;
            redisTemplate.opsForValue().set(key, "locked", duration);
            log.info("User {} locked for {}", email, duration);
        } catch (Exception e) {
            log.error("Error locking user login for email: {}", email, e);
        }
    }

    @Override
    public boolean isLoginLocked(String email) {
        try {
            String key = LOGIN_LOCKED_PREFIX + email;
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("Error checking login locked status for email: {}", email, e);
            return false;
        }
    }

    private static final String BLACKLIST_PREFIX = "token_blacklist:";

    @Override
    public void addToBlacklist(String token, long expirationInMs) {
        try {
            if (expirationInMs > 0) {
                String key = BLACKLIST_PREFIX + token;
                redisTemplate.opsForValue().set(key, "blacklisted", Duration.ofMillis(expirationInMs));
                log.info("Token added to blacklist with expiration: {} ms", expirationInMs);
            }
        } catch (Exception e) {
            log.error("Error adding token to blacklist", e);
        }
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        try {
            String key = BLACKLIST_PREFIX + token;
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("Error checking token in blacklist", e);
            return false;
        }
    }
}