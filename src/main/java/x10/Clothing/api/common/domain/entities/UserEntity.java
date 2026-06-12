package x10.Clothing.api.common.domain.entities;

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
public class UserEntity {

    private String id;

    private String username;

    private String email;

    private String passwordHash;

    private String phoneNumber;

    private String avatarUrl;

    private UserStatus status;

    private AuthProvider providerType;

    private boolean emailVerified;

    private LocalDateTime verifiedAt;
    /**
     * Danh sách role của user
     * VD: ["ADMIN"]
     * VD: ["STAFF", "USER"]
     */
    private List<String> roleIds;

    private Instant createdAt;

    private Instant updatedAt;
}
