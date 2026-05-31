package x10.Clothing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        String userId = authentication.getPrincipal().toString();

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

    @PutMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UpdateUserResponse> updateUser(
            @RequestBody UpdateUserRequest request,
            HttpServletRequest httpRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getPrincipal().toString();

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

    @GetMapping
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
}
