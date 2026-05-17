package x10.Clothing.api.service.userService;

import x10.Clothing.api.common.domain.entities.UserEntity;
import x10.Clothing.api.service.userService.createUserUc.CreateUserReq;

public interface ICoreUserService {
    UserEntity createUser(CreateUserReq user);
}
