package x10.Clothing.api.infrastructure.banner.adapter;

import x10.Clothing.api.common.domain.entities.BannerEntity;
import x10.Clothing.api.infrastructure.banner.db.mongodb.BannerDocument;

public class BannerMapper {

    public static BannerEntity toEntity(BannerDocument doc) {
        if (doc == null) return null;
        return BannerEntity.builder()
                .id(doc.getId())
                .title(doc.getTitle())
                .subtitle(doc.getSubtitle())
                .imageUrl(doc.getImageUrl())
                .linkUrl(doc.getLinkUrl())
                .active(doc.getActive())
                .sortOrder(doc.getSortOrder())
                .createdAt(doc.getCreatedAt())
                .updatedAt(doc.getUpdatedAt())
                .build();
    }

    public static BannerDocument toDocument(BannerEntity entity) {
        if (entity == null) return null;
        return BannerDocument.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .subtitle(entity.getSubtitle())
                .imageUrl(entity.getImageUrl())
                .linkUrl(entity.getLinkUrl())
                .active(entity.getActive())
                .sortOrder(entity.getSortOrder())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
