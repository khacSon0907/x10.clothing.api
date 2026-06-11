package x10.Clothing.api.service.promotionBannerService.getPromotionBannerUc;

import x10.Clothing.api.service.promotionBannerService.PromotionBannerResp;

import java.util.List;

public interface IGetPromotionBannersUc {
    List<PromotionBannerResp> getAll();
    List<PromotionBannerResp> getAllActive();
    List<PromotionBannerResp> getCurrentlyVisible();
}
