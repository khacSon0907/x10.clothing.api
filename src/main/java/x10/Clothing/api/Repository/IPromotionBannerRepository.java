package x10.Clothing.api.Repository;

import x10.Clothing.api.common.domain.entities.PromotionBannerEntity;

import java.util.List;
import java.util.Optional;

public interface IPromotionBannerRepository {
    PromotionBannerEntity save(PromotionBannerEntity promotionBanner);
    Optional<PromotionBannerEntity> findById(String id);
    List<PromotionBannerEntity> findAll();
    List<PromotionBannerEntity> findAllActive();
    void deleteById(String id);
}
