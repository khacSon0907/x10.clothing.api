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
}