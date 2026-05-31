package x10.Clothing.api.service.bannerService.getBannerUc;

import x10.Clothing.api.service.bannerService.createBannerUc.CreateBannerResp;

import java.util.List;

public interface IGetBannersUc {
    List<CreateBannerResp> getAll();
    List<CreateBannerResp> getAllActive();
}
