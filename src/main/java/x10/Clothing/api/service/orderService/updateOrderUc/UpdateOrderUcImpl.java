package x10.Clothing.api.service.orderService.updateOrderUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IOrderRepository;
import x10.Clothing.api.Repository.IProductRepository;
import x10.Clothing.api.common.domain.entities.OrderEntity;
import x10.Clothing.api.common.domain.entities.OrderItem;
import x10.Clothing.api.common.domain.entities.ProductEntity;
import x10.Clothing.api.common.domain.enums.OrderStatus;
import x10.Clothing.api.common.domain.enums.PaymentMethod;
import x10.Clothing.api.common.domain.enums.PaymentStatus;
import x10.Clothing.api.service.orderService.OrderInventoryService;
import x10.Clothing.api.service.orderService.OrderResponse;
import x10.Clothing.api.service.orderService.OrderResponseMapper;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.order.OrderError;
import x10.Clothing.api.share.exception.product.ProductError;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateOrderUcImpl implements IUpdateOrderUc {

    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;
    private final OrderInventoryService orderInventoryService;

    @Override
    public OrderResponse execute(String orderId, UpdateOrderRequest request) {
        OrderEntity order = orderRepository.findById(orderId)
                .or(() -> orderRepository.findByOrderCode(orderId))
                .orElseThrow(() -> new BusinessException(OrderError.ORDER_NOT_FOUND));

        if (request.getReceiverName() != null && !request.getReceiverName().isBlank()) {
            order.setReceiverName(request.getReceiverName());
        }
        if (request.getReceiverPhone() != null && !request.getReceiverPhone().isBlank()) {
            order.setReceiverPhone(request.getReceiverPhone());
        }
        if (request.getReceiverAddress() != null && !request.getReceiverAddress().isBlank()) {
            order.setReceiverAddress(request.getReceiverAddress());
        }
        if (request.getPaymentMethod() != null) {
            order.setPaymentMethod(request.getPaymentMethod());
        }
        if (request.getPaymentStatus() != null) {
            order.setPaymentStatus(request.getPaymentStatus());
        }
        if (request.getNote() != null) {
            order.setNote(request.getNote());
        }
        if (request.getCancelReason() != null) {
            order.setCancelReason(request.getCancelReason());
        }
        if (request.getTrackingCode() != null) {
            order.setTrackingCode(request.getTrackingCode());
        }
        if (request.getShippingProvider() != null) {
            order.setShippingProvider(request.getShippingProvider());
        }
        if (request.getShippingFee() != null) {
            order.setShippingFee(request.getShippingFee());
        }
        if (request.getDiscountAmount() != null) {
            order.setDiscountAmount(request.getDiscountAmount());
        }
        if (request.getItems() != null) {
            if (order.getStatus() != OrderStatus.PENDING) {
                throw new BusinessException(
                        OrderError.INVALID_ORDER_STATUS,
                        "Chi co the cap nhat san pham khi don hang dang cho xu ly"
                );
            }
            if (request.getItems().isEmpty()) {
                throw new BusinessException(OrderError.INVALID_ORDER_DATA);
            }
            order.setItems(request.getItems().stream()
                    .map(this::buildOrderItem)
                    .collect(Collectors.toList()));
        }

        recalculateOrder(order);

        if (request.getStatus() != null) {
            applyStatusChange(order, request.getStatus(), request.getCancelReason());
        }

        order.setUpdatedAt(LocalDateTime.now());

        return OrderResponseMapper.toResponse(orderRepository.save(order));
    }

    private void applyStatusChange(OrderEntity order, OrderStatus requestedStatus, String cancelReason) {
        if (requestedStatus == order.getStatus()) {
            return;
        }

        if (requestedStatus == OrderStatus.CONFIRMED) {
            orderInventoryService.confirmOrder(order);
            return;
        }

        if (requestedStatus == OrderStatus.CANCELLED) {
            orderInventoryService.cancelPendingOrder(order, cancelReason);
            return;
        }

        if (order.getStatus() == OrderStatus.PENDING) {
            throw new BusinessException(
                    OrderError.INVALID_ORDER_STATUS,
                    "Don hang phai duoc xac nhan truoc khi chuyen sang trang thai tiep theo"
            );
        }

        order.setStatus(requestedStatus);
    }

    private OrderItem buildOrderItem(UpdateOrderRequest.OrderItemRequest request) {
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BusinessException(ProductError.PRODUCT_NOT_FOUND));

        BigDecimal unitPrice = product.getSalePrice() != null ? product.getSalePrice() : product.getPrice();
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Gia san pham khong hop le");
        }

        return OrderItem.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productImage(request.getProductImage())
                .colorId(request.getColorId())
                .colorName(request.getColorName())
                .sizeId(request.getSizeId())
                .sizeName(request.getSizeName())
                .quantity(request.getQuantity())
                .unitPrice(unitPrice)
                .totalPrice(unitPrice.multiply(BigDecimal.valueOf(request.getQuantity())))
                .build();
    }

    private void recalculateOrder(OrderEntity order) {
        List<OrderItem> items = order.getItems();
        if (items == null || items.isEmpty()) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA);
        }

        BigDecimal subtotal = items.stream()
                .map(item -> {
                    BigDecimal totalPrice = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                    item.setTotalPrice(totalPrice);
                    return totalPrice;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shippingFee = order.getShippingFee() == null ? BigDecimal.ZERO : order.getShippingFee();
        BigDecimal discountAmount = order.getDiscountAmount() == null ? BigDecimal.ZERO : order.getDiscountAmount();
        BigDecimal totalAmount = subtotal.add(shippingFee).subtract(discountAmount);

        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Tong tien don hang khong hop le");
        }

        order.setSubtotal(subtotal);
        order.setShippingFee(shippingFee);
        order.setDiscountAmount(discountAmount);
        order.setTotalAmount(totalAmount);
    }

}
