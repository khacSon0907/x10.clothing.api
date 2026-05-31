package x10.Clothing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import x10.Clothing.api.service.bannerService.ICoreBannerService;
import x10.Clothing.api.service.bannerService.createBannerUc.CreateBannerReq;
import x10.Clothing.api.service.bannerService.createBannerUc.CreateBannerResp;
import x10.Clothing.api.service.bannerService.updateBannerUc.UpdateBannerReq;
import x10.Clothing.api.service.bannerService.updateBannerUc.UpdateBannerResp;
import x10.Clothing.api.share.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
public class BannerController {

    private final ICoreBannerService coreBannerService;

    // ─── ADMIN ONLY ──────────────────────────────────────────────────────────

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CreateBannerResp> createBanner(
            @Valid @RequestBody CreateBannerReq req,
            HttpServletRequest request
    ) {
        CreateBannerResp resp = coreBannerService.createBanner(req);
        return ApiResponse.success(
                201,
                "BANNER.CREATE_SUCCESS",
                "Tạo banner thành công",
                resp,
                request.getRequestURI(),
                null
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UpdateBannerResp> updateBanner(
            @PathVariable("id") String id,
            @Valid @RequestBody UpdateBannerReq req,
            HttpServletRequest request
    ) {
        req.setId(id);
        UpdateBannerResp resp = coreBannerService.updateBanner(req);
        return ApiResponse.success(
                200,
                "BANNER.UPDATE_SUCCESS",
                "Cập nhật banner thành công",
                resp,
                request.getRequestURI(),
                null
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBanner(
            @PathVariable("id") String id
    ) {
        coreBannerService.deleteBanner(id);
    }

    // ─── PUBLIC ──────────────────────────────────────────────────────────────

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<CreateBannerResp>> getAllBanners(
            @RequestParam(value = "activeOnly", defaultValue = "false") boolean activeOnly,
            HttpServletRequest request
    ) {
        List<CreateBannerResp> list = activeOnly
                ? coreBannerService.getAllActive()
                : coreBannerService.getAll();
        return ApiResponse.success(
                200,
                "BANNER.LIST_SUCCESS",
                "Lấy danh sách banner thành công",
                list,
                request.getRequestURI(),
                null
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CreateBannerResp> getBannerById(
            @PathVariable("id") String id,
            HttpServletRequest request
    ) {
        CreateBannerResp resp = coreBannerService.getById(id);
        return ApiResponse.success(
                200,
                "BANNER.GET_SUCCESS",
                "Lấy thông tin banner thành công",
                resp,
                request.getRequestURI(),
                null
        );
    }
}
