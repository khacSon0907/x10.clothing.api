package x10.Clothing.api.config.redis;

import java.time.Duration;

public interface IRedisService {

    void saveRegisterOtp(
            String email,
            String otp,
            Duration expiration
    );

    String getRegisterOtp(String email);

    void deleteRegisterOtp(String email);

    long incrementFailedLogin(String email);

    void resetFailedLogin(String email);

    void lockUserLogin(String email, Duration duration);

    boolean isLoginLocked(String email);

    void addToBlacklist(String token, long expirationInMs);

    boolean isTokenBlacklisted(String token);
}