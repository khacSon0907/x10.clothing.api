package x10.Clothing.api.service.cartService;

import x10.Clothing.api.common.domain.entities.CartEntity;
import x10.Clothing.api.service.cartService.addItemToCartUc.AddCartItemReq;
import x10.Clothing.api.service.cartService.updateCartItemUc.UpdateCartItemReq;

public interface ICoreCartService {
    CartEntity getCart(String userId);
    CartEntity addItem(String userId, AddCartItemReq req);
    CartEntity updateItem(String userId, String productId, UpdateCartItemReq req);
    CartEntity removeItem(String userId, String productId);
    void clearCart(String userId);
}

