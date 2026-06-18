package x10.Clothing.api.service.bannerService.getBannerUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IBannerRepository;
import x10.Clothing.api.common.domain.entities.banner.BannerEntity;
import x10.Clothing.api.service.bannerService.createBannerUc.CreateBannerResp;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetBannersImpl implements IGetBannersUc {

    private final IBannerRepository bannerRepository;

    @Override
    public List<CreateBannerResp> getAll() {
        return bannerRepository.findAll()
                .stream()
                .map(this::toResp)
                .collect(Collectors.toList());
    }

    @Override
    public List<CreateBannerResp> getAllActive() {
        return bannerRepository.findAllActive()
                .stream()
                .map(this::toResp)
                .collect(Collectors.toList());
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
