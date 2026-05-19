package x10.Clothing.api.service.userService.createUserUc;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IUserRepository;
import x10.Clothing.api.common.domain.entities.UserEntity;
import x10.Clothing.api.common.domain.enums.AuthProvider;
import x10.Clothing.api.common.domain.enums.UserStatus;
import x10.Clothing.api.config.redis.IRedisService;
import x10.Clothing.api.service.notification.OtpGenerator;
import x10.Clothing.api.service.notification.event.RegisterOtpEvent;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.user.UserError;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateUserImpl implements ICreateUserUc {

        private static final Duration REGISTER_OTP_EXPIRE = Duration.ofMinutes(5);

        private final IUserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final IRedisService redisService;
        private final OtpGenerator otpGenerator;
        private final ApplicationEventPublisher eventPublisher;

        @Override
        public UserEntity createUser(CreateUserReq req) {

                Optional<UserEntity> existedUser = userRepository.findByEmail(req.getEmail());

                if (existedUser.isPresent()) {

                        UserEntity user = existedUser.get();

                        if (user.getProviderType() == AuthProvider.GUEST ||
                                        (user.getStatus() == UserStatus.PENDING && !user.isEmailVerified())) {

                                user.setUsername(req.getUsername());
                                user.setPasswordHash(
                                                passwordEncoder.encode(req.getPassword()));
                                user.setProviderType(AuthProvider.LOCAL);
                                user.setStatus(UserStatus.PENDING);
                                user.setEmailVerified(false);
                                user.setUpdatedAt(Instant.now());

                                UserEntity savedUser = userRepository.save(user);

                                createOtpAndPublishEvent(savedUser);

                                return savedUser;
                        }

                        throw new BusinessException(UserError.EMAIL_EXISTS);
                }

                UserEntity newUser = UserEntity.builder()
                                .username(req.getUsername())
                                .email(req.getEmail())
                                .passwordHash(
                                                passwordEncoder.encode(req.getPassword()))
                                .providerType(AuthProvider.LOCAL)
                                .status(UserStatus.PENDING)
                                .createdAt(Instant.now())
                                .updatedAt(Instant.now())
                                .emailVerified(false)
                                .build();

                UserEntity savedUser = userRepository.save(newUser);

                createOtpAndPublishEvent(savedUser);

                return savedUser;
        }

        private void createOtpAndPublishEvent(UserEntity user) {

                String otp = otpGenerator.generate();

                redisService.saveRegisterOtp(
                                user.getEmail(),
                                otp,
                                REGISTER_OTP_EXPIRE);

                eventPublisher.publishEvent(
                                new RegisterOtpEvent(
                                                user.getEmail(),
                                                user.getUsername(),
                                                otp));
        }
}