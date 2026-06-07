package x10.Clothing.api.service.orderService.createOrderUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IOrderRepository;
import x10.Clothing.api.Repository.IProductRepository;
import x10.Clothing.api.common.domain.entities.OrderEntity;
import x10.Clothing.api.common.domain.entities.OrderItem;
import x10.Clothing.api.common.domain.entities.ProductEntity;
import x10.Clothing.api.common.domain.enums.OrderStatus;
import x10.Clothing.api.common.domain.enums.PaymentStatus;
import x10.Clothing.api.service.orderService.OrderResponse;
import x10.Clothing.api.service.orderService.OrderResponseMapper;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.order.OrderError;
import x10.Clothing.api.share.exception.product.ProductError;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateOrderUcImpl implements ICreateOrderUc {

    private static final DateTimeFormatter ORDER_CODE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;

    @Override
    public OrderResponse execute(CreateOrderRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA);
        }

        LocalDateTime now = LocalDateTime.now();
        List<OrderItem> items = request.getItems().stream()
                .map(this::buildOrderItem)
                .collect(Collectors.toList());

        BigDecimal subtotal = calculateSubtotal(items);
        BigDecimal shippingFee = defaultZero(request.getShippingFee());
        BigDecimal discountAmount = defaultZero(request.getDiscountAmount());
        BigDecimal totalAmount = subtotal.add(shippingFee).subtract(discountAmount);

        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Tong tien don hang khong hop le");
        }

        OrderEntity order = OrderEntity.builder()
                .id(UUID.randomUUID().toString())
                .orderCode(generateOrderCode(now))
                .userId(request.getUserId())
                .receiverName(request.getReceiverName())
                .receiverPhone(request.getReceiverPhone())
                .receiverAddress(request.getReceiverAddress())
                .items(items)
                .subtotal(subtotal)
                .shippingFee(shippingFee)
                .discountAmount(discountAmount)
                .totalAmount(totalAmount)
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(resolvePaymentStatus(request.getPaymentStatus()))
                .status(resolveOrderStatus(request.getStatus()))
                .note(request.getNote())
                .createdAt(now)
                .updatedAt(now)
                .build();

        return OrderResponseMapper.toResponse(orderRepository.save(order));
    }

    private OrderItem buildOrderItem(CreateOrderRequest.OrderItemRequest request) {
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BusinessException(ProductError.PRODUCT_NOT_FOUND));

        BigDecimal unitPrice = product.getSalePrice() != null ? product.getSalePrice() : product.getPrice();
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Gia san pham khong hop le");
        }

        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(request.getQuantity()));

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
                .totalPrice(totalPrice)
                .build();
    }

    private BigDecimal calculateSubtotal(List<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String resolvePaymentStatus(String status) {
        if (status == null || status.isBlank()) {
            return PaymentStatus.UNPAID.name();
        }

        try {
            return PaymentStatus.valueOf(status.trim().toUpperCase()).name();
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Trang thai thanh toan khong hop le");
        }
    }

    private String resolveOrderStatus(String status) {
        if (status == null || status.isBlank()) {
            return OrderStatus.PENDING.name();
        }

        try {
            return OrderStatus.valueOf(status.trim().toUpperCase()).name();
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Trang thai don hang khong hop le");
        }
    }

    private String generateOrderCode(LocalDateTime now) {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        return "ORD" + now.format(ORDER_CODE_TIME_FORMAT) + suffix;
    }
}
