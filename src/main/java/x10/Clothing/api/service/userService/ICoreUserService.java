package x10.Clothing.api.service.userService;

import x10.Clothing.api.common.domain.entities.UserEntity;
import x10.Clothing.api.service.userService.createUserUc.CreateUserReq;
import x10.Clothing.api.service.userService.getMeUc.GetMeResponse;

public interface ICoreUserService {
    UserEntity createUser(CreateUserReq user);
    
    GetMeResponse getMe(String userId);


}
