package x10.Clothing.api.infrastructure.user.adapter.mapper;

import x10.Clothing.api.common.domain.entities.UserEntity;
import x10.Clothing.api.infrastructure.user.db.mongodb.UserDocument;

public class UserMapper {

    public static UserEntity toEntity(UserDocument document) {

        if (document == null) {
            return null;
        }

        return UserEntity.builder()
                .id(document.getId())
                .username(document.getUsername())
                .email(document.getEmail())
                .passwordHash(document.getPasswordHash())
                .phoneNumber(document.getPhoneNumber())
                .status(document.getStatus())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .emailVerified(document.isEmailVerified())
                .verifiedAt(document.getVerifiedAt())
                .avatarUrl(document.getAvatarUrl())
                .providerType(document.getProviderType())

                // ROLE
                .roleIds(document.getRoleIds())

                .build();
    }

    public static UserDocument toDocument(UserEntity entity) {

        if (entity == null) {
            return null;
        }

        return UserDocument.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .passwordHash(entity.getPasswordHash())
                .phoneNumber(entity.getPhoneNumber())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .emailVerified(entity.isEmailVerified())
                .verifiedAt(entity.getVerifiedAt())
                .avatarUrl(entity.getAvatarUrl())
                .providerType(entity.getProviderType())

                // ROLE
                .roleIds(entity.getRoleIds())

                .build();
    }
}
