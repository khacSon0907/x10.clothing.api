package x10.Clothing.api.common.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.Clothing.api.common.domain.enums.AuthProvider;
import x10.Clothing.api.common.domain.enums.UserRole;
import x10.Clothing.api.common.domain.enums.UserStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    private Set<UserRole> roles;


    private Instant createdAt;

    private Instant updatedAt;
}