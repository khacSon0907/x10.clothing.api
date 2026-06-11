package x10.Clothing.api.service.promotionBannerService;

import x10.Clothing.api.common.domain.entities.PromotionBannerEntity;

public class PromotionBannerMapper {

    public static PromotionBannerResp toResp(PromotionBannerEntity entity) {
        if (entity == null) return null;
        return PromotionBannerResp.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .active(entity.isActive())
                .sortOrder(entity.getSortOrder())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .build();
    }
}
