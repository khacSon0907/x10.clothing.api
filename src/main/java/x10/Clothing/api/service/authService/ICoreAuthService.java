package x10.Clothing.api.service.authService;

import x10.Clothing.api.service.authService.registerUc.RegisterResponse;
import x10.Clothing.api.service.userService.createUserUc.CreateUserReq;

public interface ICoreAuthService {
    RegisterResponse register(CreateUserReq req);

}
