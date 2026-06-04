package x10.Clothing.api.service.cartService;

import x10.Clothing.api.service.cartService.addItemToCartUc.AddItemToCartUcReq;
import x10.Clothing.api.service.cartService.addItemToCartUc.AddItemToCartUcResp;
import x10.Clothing.api.service.cartService.getCartUc.GetCartUcResp;
import x10.Clothing.api.service.cartService.updateCartItemUc.UpdateCartItemUcReq;
import x10.Clothing.api.service.cartService.updateCartItemUc.UpdateCartItemUcResp;

public interface ICoreCartService {
    GetCartUcResp getCart(String userId);
    AddItemToCartUcResp addItem(String userId, AddItemToCartUcReq req);
    UpdateCartItemUcResp updateItem(String userId, String productId, UpdateCartItemUcReq req);
    String removeItem(String userId, String productId);
    String clearCart(String userId);
}

