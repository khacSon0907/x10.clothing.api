package x10.Clothing.api.service.authService.loginUc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.Clothing.api.common.domain.enums.UserRole;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String id;
    private String username;
    private String email;
    private Set<UserRole> roles;
    private String accessToken;

    @JsonIgnore
    private String refreshToken;
}