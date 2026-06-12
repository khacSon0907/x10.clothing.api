package x10.Clothing.api.service.userService.updateUserRolesUc;

import x10.Clothing.api.service.userService.updateUserUc.UpdateUserResponse;

import java.util.List;

public interface IUpdateUserRolesUc {

    UpdateUserResponse updateUserRoles(String userId, List<String> roleIds);
}
