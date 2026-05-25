package x10.Clothing.api.service.authService.refreshTokenUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IUserRepository;
import x10.Clothing.api.common.domain.dto.request.TokenPayload;
import x10.Clothing.api.common.domain.entities.UserEntity;
import x10.Clothing.api.common.domain.enums.UserStatus;
import x10.Clothing.api.config.jwt.IJwtService;
import x10.Clothing.api.config.redis.IRedisService;
import x10.Clothing.api.service.authService.loginUc.LoginResponse;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.user.UserError;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenUcImpl implements IRefreshTokenUc {

    private final IUserRepository userRepository;
    private final IRedisService redisService;
    private final IJwtService jwtService;

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        // 1. Kiểm tra refresh token có trống hay không
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            throw new BusinessException(UserError.INVALID_REFRESH_TOKEN);
        }

        // 2. Kiểm tra xem token có nằm trong blacklist của Redis không
        if (redisService.isTokenBlacklisted(refreshToken)) {
            log.warn("Cảnh báo: Phát hiện nỗ lực sử dụng lại Refresh Token đã bị blacklist");
            throw new BusinessException(UserError.INVALID_REFRESH_TOKEN);
        }

        // 3. Xác thực Refresh Token và giải mã payload
        TokenPayload payload;
        try {
            payload = jwtService.validateRefreshToken(refreshToken);
        } catch (Exception e) {
            log.error("Xác thực Refresh Token thất bại: {}", e.getMessage());
            throw new BusinessException(UserError.INVALID_REFRESH_TOKEN);
        }

        // 4. Tìm kiếm người dùng bằng email trong payload
        UserEntity user = userRepository.findByEmail(payload.getEmail())
                .orElseThrow(() -> {
                    log.error("Không tìm thấy người dùng với email trong token: {}", payload.getEmail());
                    return new BusinessException(UserError.USER_NOT_FOUND);
                });

        // 5. Kiểm tra trạng thái kích hoạt email của người dùng
        if (!user.isEmailVerified()) {
            throw new BusinessException(UserError.EMAIL_NOT_VERIFIED);
        }

        // 6. Kiểm tra xem tài khoản người dùng có đang hoạt động hay không
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException(UserError.USER_NOT_ACTIVE);
        }

        // 7. Tạo Access Token mới và Refresh Token mới (Xoay vòng token)
        // Prefer roles from user entity (more authoritative). If not available, use role from token payload, else default to USER.
        String roleStr = "USER";
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            roleStr = user.getRoles().stream().map(Enum::name).collect(Collectors.joining(","));
        } else if (payload.getRole() != null && !payload.getRole().trim().isEmpty()) {
            roleStr = payload.getRole();
        }

        TokenPayload newPayload = TokenPayload.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(roleStr)
                .build();

        String newAccessToken = jwtService.generateAccessToken(newPayload);
        String newRefreshToken = jwtService.generateRefreshToken(newPayload);

        // 8. Đưa Refresh Token cũ vào blacklist của Redis để ngăn chặn replay attack
        try {
            long remainingExpiration = jwtService.getRemainingExpiration(refreshToken);
            if (remainingExpiration > 0) {
                redisService.addToBlacklist(refreshToken, remainingExpiration);
                log.info("Refresh Token cũ đã được thêm vào blacklist thành công. Hạn dùng còn lại: {} ms", remainingExpiration);
            }
        } catch (Exception e) {
            log.warn("Không thể lấy thời gian hết hạn còn lại hoặc blacklist Refresh Token cũ: {}", e.getMessage());
        }

        // 9. Trả về LoginResponse
        return LoginResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
