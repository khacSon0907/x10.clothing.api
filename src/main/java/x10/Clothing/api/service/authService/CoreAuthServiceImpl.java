package x10.Clothing.api.service.authService;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.service.authService.registerUc.IRegisterUc;
import x10.Clothing.api.service.authService.registerUc.RegisterResponse;
import x10.Clothing.api.service.authService.verifyOtpUc.IVerifyOtpUc;
import x10.Clothing.api.service.authService.verifyOtpUc.VerifyOtpReq;
import x10.Clothing.api.service.authService.loginUc.ILoginUc;
import x10.Clothing.api.service.authService.loginUc.LoginReq;
import x10.Clothing.api.service.authService.loginUc.LoginResponse;
import x10.Clothing.api.service.userService.createUserUc.CreateUserReq;

@Service
@AllArgsConstructor
public class CoreAuthServiceImpl implements ICoreAuthService {

    private final IRegisterUc registerUc;
    private final IVerifyOtpUc verifyOtpUc;
    private final ILoginUc loginUc;

    @Override
    public RegisterResponse register(CreateUserReq  req) {
        return registerUc.register(req);
    }

    @Override
    public void verifyOtp(VerifyOtpReq req) {
        verifyOtpUc.verifyOtp(req);
    }

    @Override
    public LoginResponse login(LoginReq req) {
        return loginUc.login(req);
    }
}
