package x10.Clothing.api.infrastructure.role.adapter.mapper;

import x10.Clothing.api.common.domain.entities.user.RoleEntity;
import x10.Clothing.api.infrastructure.role.db.mongodb.RoleDocument;

public class RoleMapper {

    public static RoleEntity toEntity(RoleDocument document) {

        if (document == null) {
            return null;
        }

        return RoleEntity.builder()
                .id(document.getId())
                .code(document.getCode())
                .name(document.getName())
                .description(document.getDescription())
                .active(document.isActive())
                .build();
    }

    public static RoleDocument toDocument(RoleEntity entity) {

        if (entity == null) {
            return null;
        }

        return RoleDocument.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .active(entity.isActive())
                .build();
    }
}
