package x10.Clothing.api.service.userService.getMeUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IUserRepository;
import x10.Clothing.api.common.domain.entities.UserEntity;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.user.UserError;

@Service
@RequiredArgsConstructor
public class GetMeUcImpl implements IGetMeUc {

    private final IUserRepository userRepository;

    @Override
    public GetMeResponse getMe(String userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        return GetMeResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .emailVerified(user.isEmailVerified())
                .verifiedAt(user.getVerifiedAt())
                .avatarUrl(user.getAvatarUrl())
                .providerType(user.getProviderType())
                .roles(user.getRoles())
                .build();
    }
}
