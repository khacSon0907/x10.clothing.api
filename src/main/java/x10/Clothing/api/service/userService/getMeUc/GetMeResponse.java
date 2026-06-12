package x10.Clothing.api.service.userService.getMeUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.Clothing.api.common.domain.enums.AuthProvider;
import x10.Clothing.api.common.domain.enums.UserStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMeResponse {

    private String id;

    private String username;

    private String email;

    private String phoneNumber;

    private UserStatus status;

    private Instant createdAt;

    private Instant updatedAt;

    private boolean emailVerified;

    private LocalDateTime verifiedAt;

    private String avatarUrl;

    private AuthProvider providerType;

    // ROLE
    private List<String> roleIds;
}
