package x10.Clothing.api.service.authService.registerUc;

import x10.Clothing.api.service.userService.createUserUc.CreateUserReq;

public interface IRegisterUc {
    RegisterResponse register(CreateUserReq req);
}
