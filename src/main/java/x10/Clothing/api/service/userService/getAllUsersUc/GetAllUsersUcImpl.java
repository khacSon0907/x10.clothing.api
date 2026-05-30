package x10.Clothing.api.service.userService.getAllUsersUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IUserRepository;
import x10.Clothing.api.common.domain.entities.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetAllUsersUcImpl implements IGetAllUsersUc {

    private final IUserRepository userRepository;

    @Override
    public List<GetAllUsersResponse> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();

        return users.stream()
                .map(user -> GetAllUsersResponse.builder()
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
                        .build())
                .collect(Collectors.toList());
    }
}
