package x10.Clothing.api.service.cartService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.ICartRepository;
import x10.Clothing.api.Repository.IProductRepository;
import x10.Clothing.api.common.domain.entities.CartEntity;
import x10.Clothing.api.common.domain.entities.CartItem;
import x10.Clothing.api.common.domain.entities.ProductEntity;
import x10.Clothing.api.service.cartService.addItemToCartUc.AddCartItemReq;
import x10.Clothing.api.service.cartService.updateCartItemUc.UpdateCartItemReq;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CoreCartServiceImpl implements ICoreCartService {

    private final ICartRepository cartRepository;
    private final IProductRepository productRepository;

    @Override
    public CartEntity getCart(String userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    CartEntity cart = CartEntity.builder()
                            .cartId(UUID.randomUUID().toString())
                            .userId(userId)
                            .items(new ArrayList<>())
                            .totalAmount(BigDecimal.ZERO)
                            .totalQuantity(0)
                            .createdAt(Instant.now())
                            .updatedAt(Instant.now())
                            .build();
                    return cartRepository.save(cart);
                });
    }

    @Override
    public CartEntity addItem(String userId, AddCartItemReq req) {
        CartEntity cart = getCart(userId);
        Optional<ProductEntity> productOpt = productRepository.findById(req.getProductId());
        if (productOpt.isEmpty()) {
            throw new RuntimeException("PRODUCT_NOT_FOUND");
        }
        ProductEntity product = productOpt.get();
        BigDecimal unitPrice = product.getSalePrice() != null ? product.getSalePrice() : product.getPrice();

        // find existing item by productId + color + size
        List<CartItem> items = cart.getItems() == null ? new ArrayList<>() : new ArrayList<>(cart.getItems());
        Optional<CartItem> existing = items.stream()
                .filter(i -> req.getProductId().equals(i.getProductId())
                        && ((req.getColorId() == null && i.getColorId() == null) || (req.getColorId() != null && req.getColorId().equals(i.getColorId())))
                        && ((req.getSizeId() == null && i.getSizeId() == null) || (req.getSizeId() != null && req.getSizeId().equals(i.getSizeId())))
                ).findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + req.getQuantity());
            item.setUnitPrice(unitPrice);
        } else {
            CartItem item = CartItem.builder()
                    .productId(req.getProductId())
                    .colorId(req.getColorId())
                    .sizeId(req.getSizeId())
                    .quantity(req.getQuantity())
                    .unitPrice(unitPrice)
                    .build();
            items.add(item);
        }

        cart.setItems(items);
        recalcCart(cart);
        cart.setUpdatedAt(Instant.now());
        return cartRepository.save(cart);
    }

    @Override
    public CartEntity updateItem(String userId, String productId, UpdateCartItemReq req) {
        CartEntity cart = getCart(userId);
        List<CartItem> items = cart.getItems() == null ? new ArrayList<>() : new ArrayList<>(cart.getItems());
        Optional<CartItem> existing = items.stream()
                .filter(i -> productId.equals(i.getProductId())
                        && ((req.getColorId() == null && i.getColorId() == null) || (req.getColorId() != null && req.getColorId().equals(i.getColorId())))
                        && ((req.getSizeId() == null && i.getSizeId() == null) || (req.getSizeId() != null && req.getSizeId().equals(i.getSizeId())))
                ).findFirst();
        if (existing.isEmpty()) {
            throw new RuntimeException("CART_ITEM_NOT_FOUND");
        }
        CartItem item = existing.get();
        item.setQuantity(req.getQuantity());
        cart.setItems(items);
        recalcCart(cart);
        cart.setUpdatedAt(Instant.now());
        return cartRepository.save(cart);
    }

    @Override
    public CartEntity removeItem(String userId, String productId) {
        CartEntity cart = getCart(userId);
        List<CartItem> items = cart.getItems() == null ? new ArrayList<>() : new ArrayList<>(cart.getItems());
        boolean removed = items.removeIf(i -> productId.equals(i.getProductId()));
        if (!removed) {
            throw new RuntimeException("CART_ITEM_NOT_FOUND");
        }
        cart.setItems(items);
        recalcCart(cart);
        cart.setUpdatedAt(Instant.now());
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }

    private void recalcCart(CartEntity cart) {
        int totalQty = cart.getItems() == null ? 0 : cart.getItems().stream().mapToInt(CartItem::getQuantity).sum();
        cart.setTotalQuantity(totalQty);
        cart.setTotalAmount(cart.getItems() == null ? BigDecimal.ZERO : cart.getItems().stream()
                .map(i -> i.getUnitPrice().multiply(new BigDecimal(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}

