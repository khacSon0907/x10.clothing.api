package x10.Clothing.api.infrastructure.user.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import x10.Clothing.api.common.domain.enums.AuthProvider;
import x10.Clothing.api.common.domain.enums.UserStatus;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "users")
public class UserDocument {

    @Id
    private String id;
    private String username;
    private String email;
    private String phoneNumber;
    private String passwordHash;
    private UserStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean emailVerified;
    private LocalDateTime verifiedAt;
    private String avatarUrl;
    private AuthProvider providerType;
}