package x10.Clothing.api.service.roleService;

import java.util.List;

public interface ICoreRoleService {

    RoleResponse createRole(RoleRequest request);

    RoleResponse updateRole(String id, RoleRequest request);

    RoleResponse getRoleById(String id);

    List<RoleResponse> getAllRoles();
}
