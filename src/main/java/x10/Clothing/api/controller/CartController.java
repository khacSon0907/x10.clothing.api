package x10.Clothing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import x10.Clothing.api.service.cartService.ICoreCartService;
import x10.Clothing.api.service.cartService.addItemToCartUc.AddItemToCartUcReq;
import x10.Clothing.api.service.cartService.addItemToCartUc.AddItemToCartUcResp;
import x10.Clothing.api.service.cartService.getCartUc.GetCartUcResp;
import x10.Clothing.api.service.cartService.updateCartItemUc.UpdateCartItemUcReq;
import x10.Clothing.api.service.cartService.updateCartItemUc.UpdateCartItemUcResp;
import x10.Clothing.api.share.response.ApiResponse;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final ICoreCartService coreCartService;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<GetCartUcResp> getCart(
            @PathVariable("userId") String userId,
            HttpServletRequest request
    ) {
        GetCartUcResp response = coreCartService.getCart(userId);
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
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AddItemToCartUcResp> addItem(
            @PathVariable("userId") String userId,
            @Valid @RequestBody AddItemToCartUcReq req,
            HttpServletRequest request
    ) {
        AddItemToCartUcResp response = coreCartService.addItem(userId, req);
        return ApiResponse.success(
                201,
                "CART.ADD_ITEM_SUCCESS",
                "Thêm vào giỏ hàng thành công",
                response,
                request.getRequestURI(),
                null
        );
    }

    @PutMapping("/{userId}/items/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UpdateCartItemUcResp> updateItem(
            @PathVariable("userId") String userId,
            @PathVariable("productId") String productId,
            @Valid @RequestBody UpdateCartItemUcReq req,
            HttpServletRequest request
    ) {
        UpdateCartItemUcResp response = coreCartService.updateItem(userId, productId, req);
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
    public ApiResponse<String> removeItem(
            @PathVariable("userId") String userId,
            @PathVariable("productId") String productId,
            HttpServletRequest request
    ) {
        String response = coreCartService.removeItem(userId, productId);
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
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> clearCart(
            @PathVariable("userId") String userId,
            HttpServletRequest request
    ) {
        String response = coreCartService.clearCart(userId);
        return ApiResponse.success(
                200,
                "CART.CLEAR_SUCCESS",
                "Xóa toàn bộ giỏ hàng thành công",
                response,
                request.getRequestURI(),
                null
        );
    }
}

