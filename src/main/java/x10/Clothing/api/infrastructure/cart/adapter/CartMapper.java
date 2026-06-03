package x10.Clothing.api.infrastructure.cart.adapter;

import x10.Clothing.api.common.domain.entities.CartEntity;
import x10.Clothing.api.common.domain.entities.CartItem;
import x10.Clothing.api.infrastructure.cart.db.mongodb.CartDocument;
import x10.Clothing.api.infrastructure.cart.db.mongodb.CartItemDocument;

import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

    public static CartEntity toEntity(CartDocument doc) {
        if (doc == null) return null;
        List<CartItem> items = null;
        if (doc.getItems() != null) {
            items = doc.getItems().stream().map(CartMapper::toItemEntity).collect(Collectors.toList());
        }
        return CartEntity.builder()
                .cartId(doc.getId())
                .userId(doc.getUserId())
                .items(items)
                .totalQuantity(doc.getTotalQuantity())
                .totalAmount(doc.getTotalAmount())
                .createdAt(doc.getCreatedAt())
                .updatedAt(doc.getUpdatedAt())
                .build();
    }

    public static CartDocument toDocument(CartEntity entity) {
        if (entity == null) return null;
        List<CartItemDocument> items = null;
        if (entity.getItems() != null) {
            items = entity.getItems().stream().map(CartMapper::toItemDocument).collect(Collectors.toList());
        }
        return CartDocument.builder()
                .id(entity.getCartId())
                .userId(entity.getUserId())
                .items(items)
                .totalQuantity(entity.getTotalQuantity())
                .totalAmount(entity.getTotalAmount())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static CartItem toItemEntity(CartItemDocument doc) {
        if (doc == null) return null;
        return CartItem.builder()
                .productId(doc.getProductId())
                .colorId(doc.getColorId())
                .sizeId(doc.getSizeId())
                .quantity(doc.getQuantity())
                .unitPrice(doc.getUnitPrice())
                .build();
    }

    public static CartItemDocument toItemDocument(CartItem entity) {
        if (entity == null) return null;
        return CartItemDocument.builder()
                .productId(entity.getProductId())
                .colorId(entity.getColorId())
                .sizeId(entity.getSizeId())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .build();
    }
}

