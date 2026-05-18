package x10.Clothing.api.service.authService;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.service.authService.registerUc.IRegisterUc;
import x10.Clothing.api.service.authService.registerUc.RegisterResponse;
import x10.Clothing.api.service.userService.createUserUc.CreateUserReq;

@Service
@AllArgsConstructor
public class CoreAuthServiceImpl implements ICoreAuthService {

    private final IRegisterUc registerUc;

    @Override
    public RegisterResponse register(CreateUserReq  req) {
        return registerUc.register(req);
    }

}
