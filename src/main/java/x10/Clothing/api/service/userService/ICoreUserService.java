package x10.Clothing.api.service.userService;

import x10.Clothing.api.common.domain.entities.UserEntity;
import x10.Clothing.api.service.userService.createUserUc.CreateUserReq;
import x10.Clothing.api.service.userService.getMeUc.GetMeResponse;
import x10.Clothing.api.service.userService.updateUserUc.UpdateUserRequest;
import x10.Clothing.api.service.userService.updateUserUc.UpdateUserResponse;

public interface ICoreUserService {
    UserEntity createUser(CreateUserReq user);
    
    GetMeResponse getMe(String userId);

    UpdateUserResponse updateUser(String userId, UpdateUserRequest request);
}
