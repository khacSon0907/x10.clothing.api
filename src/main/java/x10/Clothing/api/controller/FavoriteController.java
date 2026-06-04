package x10.Clothing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import x10.Clothing.api.service.favoriteService.ICoreFavoriteService;
import x10.Clothing.api.service.favoriteService.addFavoriteItemUc.AddFavoriteItemReq;
import x10.Clothing.api.service.favoriteService.addFavoriteItemUc.AddFavoriteItemResp;
import x10.Clothing.api.service.favoriteService.getFavoriteUc.GetFavoriteUcResp;
import x10.Clothing.api.share.response.ApiResponse;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final ICoreFavoriteService coreFavoriteService;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<GetFavoriteUcResp> getFavorite(
            @PathVariable("userId") String userId,
            HttpServletRequest request
    ) {
        GetFavoriteUcResp response = coreFavoriteService.getFavorite(userId);
        return ApiResponse.success(
                200,
                "FAVORITE.GET_SUCCESS",
                "Lấy danh sách yêu thích thành công",
                response,
                request.getRequestURI(),
                null
        );
    }

    @PostMapping("/{userId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AddFavoriteItemResp> addItem(
            @PathVariable("userId") String userId,
            @Valid @RequestBody AddFavoriteItemReq req,
            HttpServletRequest request
    ) {
        AddFavoriteItemResp response = coreFavoriteService.addItem(userId, req);
        return ApiResponse.success(
                201,
                "FAVORITE.ADD_ITEM_SUCCESS",
                "Thêm vào danh sách yêu thích thành công",
                response,
                request.getRequestURI(),
                null
        );
    }

    @DeleteMapping("/{userId}/items/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> removeItem(
            @PathVariable("userId") String userId,
            @PathVariable("productId") String productId,
            HttpServletRequest request
    ) {
        String response = coreFavoriteService.removeItem(userId, productId);
        return ApiResponse.success(
                200,
                "FAVORITE.REMOVE_ITEM_SUCCESS",
                "Xóa sản phẩm khỏi danh sách yêu thích thành công",
                response,
                request.getRequestURI(),
                null
        );
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> clearFavorite(
            @PathVariable("userId") String userId,
            HttpServletRequest request
    ) {
        String response = coreFavoriteService.clearFavorite(userId);
        return ApiResponse.success(
                200,
                "FAVORITE.CLEAR_SUCCESS",
                "Xóa toàn bộ danh sách yêu thích thành công",
                response,
                request.getRequestURI(),
                null
        );
    }
}

