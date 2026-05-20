package x10.Clothing.api.service.authService.refreshTokenUc;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenReq {

    @NotBlank(message = "Refresh token không được để trống")
    private String refreshToken;
}
