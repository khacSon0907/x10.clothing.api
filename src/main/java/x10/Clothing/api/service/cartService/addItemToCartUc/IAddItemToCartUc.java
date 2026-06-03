package x10.Clothing.api.service.cartService.addItemToCartUc;

import x10.Clothing.api.common.domain.entities.CartEntity;

public interface IAddItemToCartUc {
    CartEntity addItem(String userId, AddCartItemReq req);
}

