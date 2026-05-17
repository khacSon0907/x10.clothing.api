package x10.Clothing.api.service.userService.createUserUc;

import x10.Clothing.api.common.domain.entities.UserEntity;

public interface ICreateUserUc {

    UserEntity createUser(CreateUserReq user);
}
