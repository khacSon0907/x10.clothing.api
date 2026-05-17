package x10.Clothing.api.service.userService.createUserUc;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IUserRepository;
import x10.Clothing.api.common.domain.entities.UserEntity;
import x10.Clothing.api.common.domain.enums.AuthProvider;
import x10.Clothing.api.common.domain.enums.UserStatus;

import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateUserImpl implements ICreateUserUc {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity createUser(CreateUserReq req) {

        Optional<UserEntity> existedUser =
                userRepository.findByEmail(req.getEmail());

        // Nếu email đã tồn tại
        if (existedUser.isPresent()) {

            UserEntity user = existedUser.get();

            // Guest -> convert thành LOCAL
            if (user.getProviderType() == AuthProvider.GUEST) {

                user.setUsername(req.getUsername());
                user.setPasswordHash(
                        passwordEncoder.encode(req.getPassword())
                );
                user.setProviderType(AuthProvider.LOCAL);
                user.setUpdatedAt(Instant.now());

                return userRepository.save(user);
            }

            throw new RuntimeException("Email already exists");
        }

        // User mới
        UserEntity newUser = UserEntity.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .passwordHash(
                        passwordEncoder.encode(req.getPassword())
                )
                .providerType(AuthProvider.LOCAL)
                .status(UserStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .emailVerified(false)
                .build();

        return userRepository.save(newUser);
    }
}