package x10.Clothing.api.Repository;

import x10.Clothing.api.common.domain.entities.cart.CartEntity;

import java.util.Optional;

public interface ICartRepository {

    Optional<CartEntity> findByUserId(String userId);

    CartEntity save(CartEntity cart);

    void deleteByUserId(String userId);

}

