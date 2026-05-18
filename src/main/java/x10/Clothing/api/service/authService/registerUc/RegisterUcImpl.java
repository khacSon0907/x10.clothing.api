package x10.Clothing.api.service.authService.registerUc;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.common.domain.entities.UserEntity;
import x10.Clothing.api.service.userService.ICoreUserService;
import x10.Clothing.api.service.userService.createUserUc.CreateUserReq;

@Service
@AllArgsConstructor
public class RegisterUcImpl implements IRegisterUc {

    private final ICoreUserService coreUserService;

    @Override
    public RegisterResponse register(CreateUserReq req) {

        UserEntity createdUser = coreUserService.createUser(req);

        return RegisterResponse.builder()
                .id(createdUser.getId())
                .username(createdUser.getUsername())
                .email(createdUser.getEmail())
                .build();
    }
}