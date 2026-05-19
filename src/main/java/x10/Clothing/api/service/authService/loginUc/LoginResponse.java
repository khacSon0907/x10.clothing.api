package x10.Clothing.api.service.authService.loginUc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String id;
    private String username;
    private String email;
    private String accessToken;

    @JsonIgnore
    private String refreshToken;
}
