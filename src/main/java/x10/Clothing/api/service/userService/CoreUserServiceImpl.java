package x10.Clothing.api.service.userService;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.common.domain.entities.UserEntity;
import x10.Clothing.api.service.userService.createUserUc.CreateUserReq;
import x10.Clothing.api.service.userService.createUserUc.ICreateUserUc;

@Service
@AllArgsConstructor
public class CoreUserServiceImpl implements ICoreUserService{

    private final ICreateUserUc createUserUC;


    @Override
    public UserEntity createUser(CreateUserReq user) {
        return createUserUC.createUser(user);
    }
}
