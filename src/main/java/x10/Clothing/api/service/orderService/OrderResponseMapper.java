package x10.Clothing.api.service.orderService;

import x10.Clothing.api.common.domain.entities.OrderEntity;
import x10.Clothing.api.common.domain.entities.OrderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponseMapper {

    public static OrderResponse toResponse(OrderEntity order) {
        if (order == null) {
            return null;
        }

        List<OrderResponse.OrderItemResponse> items = order.getItems() == null
                ? new ArrayList<>()
                : order.getItems().stream()
                .map(OrderResponseMapper::toItemResponse)
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .userId(order.getUserId())
                .receiverName(order.getReceiverName())
                .receiverPhone(order.getReceiverPhone())
                .receiverAddress(order.getReceiverAddress())
                .items(items)
                .subtotal(order.getSubtotal())
                .shippingFee(order.getShippingFee())
                .discountAmount(order.getDiscountAmount())
                .totalAmount(order.getTotalAmount())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .status(order.getStatus())
                .note(order.getNote())
                .cancelReason(order.getCancelReason())
                .trackingCode(order.getTrackingCode())
                .shippingProvider(order.getShippingProvider())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private static OrderResponse.OrderItemResponse toItemResponse(OrderItem item) {
        if (item == null) {
            return null;
        }

        return OrderResponse.OrderItemResponse.builder()
                .productId(item.getProductId())
                .productName(item.getProductName())
                .productImage(item.getProductImage())
                .colorId(item.getColorId())
                .colorName(item.getColorName())
                .sizeId(item.getSizeId())
                .sizeName(item.getSizeName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .totalPrice(item.getTotalPrice())
                .build();
    }
}
