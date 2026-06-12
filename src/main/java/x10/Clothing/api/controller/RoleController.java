package x10.Clothing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import x10.Clothing.api.service.roleService.ICoreRoleService;
import x10.Clothing.api.service.roleService.RoleRequest;
import x10.Clothing.api.service.roleService.RoleResponse;
import x10.Clothing.api.share.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final ICoreRoleService coreRoleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RoleResponse> createRole(
            @Valid @RequestBody RoleRequest request,
            HttpServletRequest httpRequest
    ) {

        RoleResponse response = coreRoleService.createRole(request);

        return ApiResponse.success(
                201,
                "ROLE.CREATE_SUCCESS",
                "Tao role thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<RoleResponse>> getAllRoles(HttpServletRequest request) {

        List<RoleResponse> response = coreRoleService.getAllRoles();

        return ApiResponse.success(
                200,
                "ROLE.GET_ALL_SUCCESS",
                "Lay danh sach role thanh cong",
                response,
                request.getRequestURI(),
                null
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<RoleResponse> getRoleById(
            @PathVariable String id,
            HttpServletRequest request
    ) {

        RoleResponse response = coreRoleService.getRoleById(id);

        return ApiResponse.success(
                200,
                "ROLE.GET_SUCCESS",
                "Lay thong tin role thanh cong",
                response,
                request.getRequestURI(),
                null
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<RoleResponse> updateRole(
            @PathVariable String id,
            @Valid @RequestBody RoleRequest request,
            HttpServletRequest httpRequest
    ) {

        RoleResponse response = coreRoleService.updateRole(id, request);

        return ApiResponse.success(
                200,
                "ROLE.UPDATE_SUCCESS",
                "Cap nhat role thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }
}
