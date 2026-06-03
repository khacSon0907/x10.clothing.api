package x10.Clothing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import x10.Clothing.api.common.domain.entities.CartEntity;
import x10.Clothing.api.service.cartService.ICoreCartService;
import x10.Clothing.api.service.cartService.addItemToCartUc.AddCartItemReq;
import x10.Clothing.api.service.cartService.updateCartItemUc.UpdateCartItemReq;
import x10.Clothing.api.share.response.ApiResponse;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final ICoreCartService coreCartService;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CartEntity> getCart(@PathVariable("userId") String userId, HttpServletRequest request) {
        CartEntity response = coreCartService.getCart(userId);
        return ApiResponse.success(
                200,
                "CART.GET_SUCCESS",
                "Lấy giỏ hàng thành công",
                response,
                request.getRequestURI(),
                null
        );
    }

    @PostMapping("/{userId}/items")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CartEntity> addItem(
            @PathVariable("userId") String userId,
            @Valid @RequestBody AddCartItemReq req,
            HttpServletRequest request
    ) {
        CartEntity response = coreCartService.addItem(userId, req);
        return ApiResponse.success(
                200,
                "CART.ADD_ITEM_SUCCESS",
                "Thêm vào giỏ hàng thành công",
                response,
                request.getRequestURI(),
                null
        );
    }

    @PutMapping("/{userId}/items/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CartEntity> updateItem(
            @PathVariable("userId") String userId,
            @PathVariable("productId") String productId,
            @Valid @RequestBody UpdateCartItemReq req,
            HttpServletRequest request
    ) {
        CartEntity response = coreCartService.updateItem(userId, productId, req);
        return ApiResponse.success(
                200,
                "CART.UPDATE_ITEM_SUCCESS",
                "Cập nhật giỏ hàng thành công",
                response,
                request.getRequestURI(),
                null
        );
    }

    @DeleteMapping("/{userId}/items/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CartEntity> removeItem(
            @PathVariable("userId") String userId,
            @PathVariable("productId") String productId,
            HttpServletRequest request
    ) {
        CartEntity response = coreCartService.removeItem(userId, productId);
        return ApiResponse.success(
                200,
                "CART.REMOVE_ITEM_SUCCESS",
                "Xóa sản phẩm khỏi giỏ hàng thành công",
                response,
                request.getRequestURI(),
                null
        );
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@PathVariable("userId") String userId) {
        coreCartService.clearCart(userId);
    }
}

