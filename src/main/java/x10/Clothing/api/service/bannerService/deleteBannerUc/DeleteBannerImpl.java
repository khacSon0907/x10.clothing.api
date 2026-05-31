package x10.Clothing.api.service.bannerService.deleteBannerUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IBannerRepository;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.banner.BannerError;

@Service
@RequiredArgsConstructor
public class DeleteBannerImpl implements IDeleteBannerUc {

    private final IBannerRepository bannerRepository;

    @Override
    public void deleteBanner(String bannerId) {

        if (bannerId == null || bannerId.isBlank()) {
            throw new IllegalArgumentException("Banner ID là bắt buộc");
        }

        bannerRepository.findById(bannerId)
                .orElseThrow(() -> new BusinessException(BannerError.BANNER_NOT_FOUND));

        bannerRepository.deleteById(bannerId);
    }
}
