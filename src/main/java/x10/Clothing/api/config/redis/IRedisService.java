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
}