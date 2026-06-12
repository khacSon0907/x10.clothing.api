package x10.Clothing.api.service.userService.updateUserUc;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import x10.Clothing.api.Repository.IUserRepository;
import x10.Clothing.api.common.domain.entities.UserEntity;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.user.UserError;

import java.time.Instant;

@Component
@AllArgsConstructor
public class UpdateUserUcImpl implements IUpdateUserUc {

    private final IUserRepository userRepository;

    @Override
    public UpdateUserResponse updateUser(String userId, UpdateUserRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        // Update username
        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            user.setUsername(request.getUsername());
        }

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAvatarUrl() != null && !request.getAvatarUrl().isEmpty()) {
            user.setAvatarUrl(request.getAvatarUrl());
        }

        // Set updated timestamp
        user.setUpdatedAt(Instant.now());

        // Save the updated user
        UserEntity updatedUser = userRepository.save(user);

        // Build and return response
        return UpdateUserResponse.builder()
                .id(updatedUser.getId())
                .username(updatedUser.getUsername())
                .email(updatedUser.getEmail())
                .phoneNumber(updatedUser.getPhoneNumber())
                .status(updatedUser.getStatus())
                .createdAt(updatedUser.getCreatedAt())
                .updatedAt(updatedUser.getUpdatedAt())
                .emailVerified(updatedUser.isEmailVerified())
                .verifiedAt(updatedUser.getVerifiedAt())
                .avatarUrl(updatedUser.getAvatarUrl())
                .providerType(updatedUser.getProviderType())
                .roleIds(updatedUser.getRoleIds())
                .build();
    }
}
