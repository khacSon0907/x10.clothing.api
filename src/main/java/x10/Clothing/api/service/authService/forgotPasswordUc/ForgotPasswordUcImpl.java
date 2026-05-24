package x10.Clothing.api.service.authService.forgotPasswordUc;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IUserRepository;
import x10.Clothing.api.common.domain.entities.UserEntity;
import x10.Clothing.api.config.redis.IRedisService;
import x10.Clothing.api.service.notification.OtpGenerator;
import x10.Clothing.api.service.notification.event.ForgotPasswordOtpEvent;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.user.UserError;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ForgotPasswordUcImpl implements IForgotPasswordUc {

    private static final Duration FORGOT_PASSWORD_OTP_EXPIRE = Duration.ofMinutes(5);

    private final IUserRepository userRepository;
    private final IRedisService redisService;
    private final OtpGenerator otpGenerator;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void forgotPassword(ForgotPasswordReq req) {
        if (redisService.isForgotPasswordCooldown(req.getEmail())) {
            throw new BusinessException(UserError.RATE_LIMIT_EXCEEDED, "Vui lòng đợi 5 phút để yêu cầu lại mã xác nhận.");
        }

        UserEntity user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        String otp = otpGenerator.generate();

        redisService.saveForgotPasswordOtp(
                user.getEmail(),
                otp,
                FORGOT_PASSWORD_OTP_EXPIRE
        );

        redisService.saveForgotPasswordCooldown(
                user.getEmail(),
                Duration.ofMinutes(5)
        );

        eventPublisher.publishEvent(
                new ForgotPasswordOtpEvent(
                        user.getEmail(),
                        user.getUsername(),
                        otp
                )
        );
    }
}
