package x10.Clothing.api.service.cartService.getCartUc;

import x10.Clothing.api.common.domain.entities.CartEntity;

public interface IGetCartUc {
    CartEntity getOrCreateCart(String userId);
}

