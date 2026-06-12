package x10.Clothing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import x10.Clothing.api.service.userService.ICoreUserService;
import x10.Clothing.api.service.userService.getAllUsersUc.GetAllUsersResponse;
import x10.Clothing.api.service.userService.getMeUc.GetMeResponse;
import x10.Clothing.api.service.userService.updateUserUc.UpdateUserRequest;
import x10.Clothing.api.service.userService.updateUserUc.UpdateUserResponse;
import x10.Clothing.api.service.userService.updateUserRolesUc.UpdateUserRolesRequest;
import x10.Clothing.api.share.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final ICoreUserService coreUserService;

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<GetMeResponse> getMe(HttpServletRequest request) {
        String userId = getCurrentUserId();
        System.out.println("Current user ID api get me : " + userId); // Debug log
        GetMeResponse response = coreUserService.getMe(userId);
        return ApiResponse.success(
                200,
                "USER.GET_ME_SUCCESS",
                "Lấy thông tin người dùng thành công",
                response,
                request.getRequestURI(),
                null
        );
    }

    @PutMapping("/me/update")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UpdateUserResponse> updateUser(
            @RequestBody UpdateUserRequest request,
            HttpServletRequest httpRequest
    ) {
        String userId = getCurrentUserId();

        UpdateUserResponse response = coreUserService.updateUser(userId, request);

        return ApiResponse.success(
                200,
                "USER.UPDATE_SUCCESS",
                "Cập nhật thông tin người dùng thành công",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<GetAllUsersResponse>> getAllUsers(HttpServletRequest request) {
        List<GetAllUsersResponse> response = coreUserService.getAllUsers();

        return ApiResponse.success(
                200,
                "USER.GET_ALL_SUCCESS",
                "Lấy danh sách người dùng thành công",
                response,
                request.getRequestURI(),
                null
        );
    }

    @PutMapping("/{id}/roles")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UpdateUserResponse> updateUserRoles(
            @PathVariable("id") String id,
            @Valid @RequestBody UpdateUserRolesRequest request,
            HttpServletRequest httpRequest
    ) {
        UpdateUserResponse response = coreUserService.updateUserRoles(id, request.getRoleIds());

        return ApiResponse.success(
                200,
                "USER.UPDATE_ROLES_SUCCESS",
                "Cap nhat role cua user thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication.getPrincipal() == null ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User is not authenticated"
            );
        }

        return authentication.getPrincipal().toString();
    }
}
