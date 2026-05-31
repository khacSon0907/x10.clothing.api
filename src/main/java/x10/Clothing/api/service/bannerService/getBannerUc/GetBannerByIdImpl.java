package x10.Clothing.api.service.bannerService.getBannerUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IBannerRepository;
import x10.Clothing.api.common.domain.entities.BannerEntity;
import x10.Clothing.api.service.bannerService.createBannerUc.CreateBannerResp;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.banner.BannerError;

@Service
@RequiredArgsConstructor
public class GetBannerByIdImpl implements IGetBannerByIdUc {

    private final IBannerRepository bannerRepository;

    @Override
    public CreateBannerResp getById(String id) {
        BannerEntity entity = bannerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BannerError.BANNER_NOT_FOUND));
        return toResp(entity);
    }

    private CreateBannerResp toResp(BannerEntity e) {
        return CreateBannerResp.builder()
                .id(e.getId())
                .title(e.getTitle())
                .subtitle(e.getSubtitle())
                .imageUrl(e.getImageUrl())
                .linkUrl(e.getLinkUrl())
                .active(e.getActive())
                .sortOrder(e.getSortOrder())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
