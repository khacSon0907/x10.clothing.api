package x10.Clothing.api.service.orderService.createOrderUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IOrderRepository;
import x10.Clothing.api.Repository.IProductRepository;
import x10.Clothing.api.common.domain.entities.OrderEntity;
import x10.Clothing.api.common.domain.entities.OrderItem;
import x10.Clothing.api.common.domain.entities.ProductEntity;
import x10.Clothing.api.common.domain.enums.PaymentMethod;
import x10.Clothing.api.service.orderService.OrderResponse;
import x10.Clothing.api.service.orderService.OrderResponseMapper;
import x10.Clothing.api.service.paymentService.ICorePaymentService;
import x10.Clothing.api.service.paymentService.createPayment.CreatePaymentLinkRequest;
import x10.Clothing.api.service.paymentService.createPayment.CreatePaymentLinkResponse;
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
    private final ICorePaymentService paymentService;

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

        String paymentMethod = resolvePaymentMethod(request.getPaymentMethod());

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
                .paymentMethod(paymentMethod)
                .paymentStatus(resolvePaymentStatus(null))
                .status(resolveOrderStatus(null))
                .note(request.getNote())
                .createdAt(now)
                .updatedAt(now)
                .build();

        OrderEntity savedOrder = orderRepository.save(order);
        OrderResponse response = OrderResponseMapper.toResponse(savedOrder);

        if (PaymentMethod.PAYOS.name().equals(paymentMethod)) {
            CreatePaymentLinkRequest paymentRequest = new CreatePaymentLinkRequest();
            paymentRequest.setOrderId(savedOrder.getId());
            CreatePaymentLinkResponse paymentResponse = paymentService.createPaymentLink(paymentRequest);
            response.setPayment(paymentResponse);
        }

        return response;
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

    private String resolvePaymentMethod(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.isBlank()) {
            return PaymentMethod.COD.name();
        }

        String upperPaymentMethod = paymentMethod.trim().toUpperCase();
        try {
            return PaymentMethod.valueOf(upperPaymentMethod).name();
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Phuong thuc thanh toan khong hop le");
        }
    }

    private String resolvePaymentStatus(String status) {
        if (status == null || status.isBlank()) {
            return "UNPAID";
        }

        String upperStatus = status.trim().toUpperCase();
        if (!isValidPaymentStatus(upperStatus)) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Trang thai thanh toan khong hop le");
        }

        return upperStatus;
    }

    private String resolveOrderStatus(String status) {
        if (status == null || status.isBlank()) {
            return "PENDING";
        }

        String upperStatus = status.trim().toUpperCase();
        if (!isValidOrderStatus(upperStatus)) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Trang thai don hang khong hop le");
        }

        return upperStatus;
    }

    private boolean isValidPaymentStatus(String status) {
        return status.equals("UNPAID") || status.equals("PAID") || status.equals("REFUNDED") || status.equals("FAILED");
    }

    private boolean isValidOrderStatus(String status) {
        return status.equals("PENDING") || status.equals("CONFIRMED") || status.equals("PROCESSING")
                || status.equals("SHIPPING") || status.equals("DELIVERED") || status.equals("CANCELLED")
                || status.equals("RETURNED");
    }

    private String generateOrderCode(LocalDateTime now) {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        return "ORD" + now.format(ORDER_CODE_TIME_FORMAT) + suffix;
    }
}
