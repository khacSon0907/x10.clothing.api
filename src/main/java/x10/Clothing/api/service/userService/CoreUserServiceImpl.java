package x10.Clothing.api.service.userService;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.common.domain.entities.UserEntity;
import x10.Clothing.api.service.userService.createUserUc.CreateUserReq;
import x10.Clothing.api.service.userService.createUserUc.ICreateUserUc;

import x10.Clothing.api.service.userService.getMeUc.IGetMeUc;
import x10.Clothing.api.service.userService.getMeUc.GetMeResponse;
import x10.Clothing.api.service.userService.updateUserUc.IUpdateUserUc;
import x10.Clothing.api.service.userService.updateUserUc.UpdateUserRequest;
import x10.Clothing.api.service.userService.updateUserUc.UpdateUserResponse;

@Service
@AllArgsConstructor
public class CoreUserServiceImpl implements ICoreUserService{

    private final ICreateUserUc createUserUC;
    private final IGetMeUc getMeUc;
    private final IUpdateUserUc updateUserUc;

    @Override
    public UserEntity createUser(CreateUserReq user) {
        return createUserUC.createUser(user);
    }

    @Override
    public GetMeResponse getMe(String userId) {
        return getMeUc.getMe(userId);
    }

    @Override
    public UpdateUserResponse updateUser(String userId, UpdateUserRequest request) {
        return updateUserUc.updateUser(userId, request);
    }
}
