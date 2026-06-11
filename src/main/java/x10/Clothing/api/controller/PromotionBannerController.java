package x10.Clothing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import x10.Clothing.api.service.promotionBannerService.ICorePromotionBannerService;
import x10.Clothing.api.service.promotionBannerService.PromotionBannerResp;
import x10.Clothing.api.service.promotionBannerService.createPromotionBannerUc.CreatePromotionBannerReq;
import x10.Clothing.api.service.promotionBannerService.updatePromotionBannerUc.UpdatePromotionBannerReq;
import x10.Clothing.api.share.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/promotion-banners")
@RequiredArgsConstructor
public class PromotionBannerController {

    private final ICorePromotionBannerService corePromotionBannerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PromotionBannerResp> createPromotionBanner(
            @Valid @RequestBody CreatePromotionBannerReq req,
            HttpServletRequest request
    ) {
        PromotionBannerResp resp = corePromotionBannerService.createPromotionBanner(req);
        return ApiResponse.success(
                201,
                "PROMOTION_BANNER.CREATE_SUCCESS",
                "Tạo promotion banner thành công",
                resp,
                request.getRequestURI(),
                null
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PromotionBannerResp> updatePromotionBanner(
            @PathVariable("id") String id,
            @Valid @RequestBody UpdatePromotionBannerReq req,
            HttpServletRequest request
    ) {
        req.setId(id);
        PromotionBannerResp resp = corePromotionBannerService.updatePromotionBanner(req);
        return ApiResponse.success(
                200,
                "PROMOTION_BANNER.UPDATE_SUCCESS",
                "Cập nhật promotion banner thành công",
                resp,
                request.getRequestURI(),
                null
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePromotionBanner(@PathVariable("id") String id) {
        corePromotionBannerService.deletePromotionBanner(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<PromotionBannerResp>> getAllPromotionBanners(
            @RequestParam(value = "activeOnly", defaultValue = "false") boolean activeOnly,
            HttpServletRequest request
    ) {
        List<PromotionBannerResp> list = activeOnly
                ? corePromotionBannerService.getCurrentlyVisible()
                : corePromotionBannerService.getAll();
        return ApiResponse.success(
                200,
                "PROMOTION_BANNER.LIST_SUCCESS",
                "Lấy danh sách promotion banner thành công",
                list,
                request.getRequestURI(),
                null
        );
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<PromotionBannerResp>> getVisiblePromotionBanners(
            HttpServletRequest request
    ) {
        List<PromotionBannerResp> list = corePromotionBannerService.getCurrentlyVisible();
        return ApiResponse.success(
                200,
                "PROMOTION_BANNER.ACTIVE_LIST_SUCCESS",
                "Lấy danh sách promotion banner đang hiển thị thành công",
                list,
                request.getRequestURI(),
                null
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PromotionBannerResp> getPromotionBannerById(
            @PathVariable("id") String id,
            HttpServletRequest request
    ) {
        PromotionBannerResp resp = corePromotionBannerService.getById(id);
        return ApiResponse.success(
                200,
                "PROMOTION_BANNER.GET_SUCCESS",
                "Lấy thông tin promotion banner thành công",
                resp,
                request.getRequestURI(),
                null
        );
    }
}
