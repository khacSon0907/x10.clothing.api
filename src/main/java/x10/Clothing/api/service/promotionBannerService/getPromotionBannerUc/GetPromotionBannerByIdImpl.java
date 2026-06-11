package x10.Clothing.api.service.promotionBannerService.getPromotionBannerUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IPromotionBannerRepository;
import x10.Clothing.api.common.domain.entities.PromotionBannerEntity;
import x10.Clothing.api.service.promotionBannerService.PromotionBannerMapper;
import x10.Clothing.api.service.promotionBannerService.PromotionBannerResp;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.promotionBanner.PromotionBannerError;

@Service
@RequiredArgsConstructor
public class GetPromotionBannerByIdImpl implements IGetPromotionBannerByIdUc {

    private final IPromotionBannerRepository promotionBannerRepository;

    @Override
    public PromotionBannerResp getById(String id) {
        PromotionBannerEntity entity = promotionBannerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(PromotionBannerError.PROMOTION_BANNER_NOT_FOUND));
        return PromotionBannerMapper.toResp(entity);
    }
}
