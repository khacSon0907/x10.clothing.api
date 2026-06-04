package x10.Clothing.api.service.cartService.updateCartItemUc;

public interface IUpdateCartItemUc {
    UpdateCartItemUcResp execute(String userId, String productId, UpdateCartItemUcReq req);
}

