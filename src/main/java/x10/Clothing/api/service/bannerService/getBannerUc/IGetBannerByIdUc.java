package x10.Clothing.api.service.bannerService.getBannerUc;

import x10.Clothing.api.service.bannerService.createBannerUc.CreateBannerResp;

public interface IGetBannerByIdUc {
    CreateBannerResp getById(String id);
}
