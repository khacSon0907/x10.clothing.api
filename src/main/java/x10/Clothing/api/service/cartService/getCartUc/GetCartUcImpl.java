package x10.Clothing.api.service.cartService.getCartUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.Clothing.api.Repository.ICartRepository;
import x10.Clothing.api.common.domain.entities.CartEntity;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetCartUcImpl implements IGetCartUc {

    private final ICartRepository cartRepository;

    @Override
    public CartEntity getOrCreateCart(String userId) {
        Optional<CartEntity> existing = cartRepository.findByUserId(userId);
        if (existing.isPresent()) return existing.get();

        CartEntity cart = CartEntity.builder()
                .cartId(UUID.randomUUID().toString())
                .userId(userId)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .totalAmount(java.math.BigDecimal.ZERO)
                .totalQuantity(0)
                .items(java.util.Collections.emptyList())
                .build();

        return cartRepository.save(cart);
    }
}

