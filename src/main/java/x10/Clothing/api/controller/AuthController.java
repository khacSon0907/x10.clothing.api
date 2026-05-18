package x10.Clothing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import x10.Clothing.api.service.authService.ICoreAuthService;
import x10.Clothing.api.service.authService.registerUc.RegisterResponse;
import x10.Clothing.api.service.userService.createUserUc.CreateUserReq;
import x10.Clothing.api.share.response.ApiResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ICoreAuthService coreAuthService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RegisterResponse> register(
            @Valid @RequestBody CreateUserReq req,
            HttpServletRequest request
    ) {
        RegisterResponse response = coreAuthService.register(req);
        return ApiResponse.success(
                201,
                "AUTH.REGISTER_SUCCESS",
                "Đăng ký thành công",
                response,
                request.getRequestURI(),
                null
        );
    }
}