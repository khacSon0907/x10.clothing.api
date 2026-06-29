package x10.Clothing.api.service.refundService.requestRefundUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IOrderRepository;
import x10.Clothing.api.Repository.IPaymentRepository;
import x10.Clothing.api.Repository.IProductRepository;
import x10.Clothing.api.Repository.IRefundRepository;
import x10.Clothing.api.common.domain.entities.order.OrderEntity;
import x10.Clothing.api.common.domain.entities.order.OrderItem;
import x10.Clothing.api.common.domain.entities.order.PaymentEntity;
import x10.Clothing.api.common.domain.entities.order.RefundEntity;
import x10.Clothing.api.common.domain.entities.product.ColorVariantEntity;
import x10.Clothing.api.common.domain.entities.product.ProductEntity;
import x10.Clothing.api.common.domain.entities.product.SizeVariantEntity;
import x10.Clothing.api.common.domain.enums.OrderStatus;
import x10.Clothing.api.common.domain.enums.PaymentMethod;
import x10.Clothing.api.common.domain.enums.RefundStatus;
import x10.Clothing.api.common.domain.enums.RefundType;
import x10.Clothing.api.service.refundService.RefundRequest;
import x10.Clothing.api.service.refundService.RefundResponse;
import x10.Clothing.api.service.refundService.RefundResponseMapper;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.order.OrderError;
import x10.Clothing.api.share.exception.refund.RefundError;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequestRefundUcImpl implements IRequestRefundUc {

    private static final EnumSet<RefundStatus> OPEN_STATUSES = EnumSet.of(
            RefundStatus.PENDING,
            RefundStatus.APPROVED,
            RefundStatus.RETURN_RECEIVED,
            RefundStatus.EXCHANGE_APPROVED,
            RefundStatus.REFUND_REQUIRED,
            RefundStatus.PROCESSING
    );

    private final IOrderRepository orderRepository;
    private final IPaymentRepository paymentRepository;
    private final IRefundRepository refundRepository;
    private final IProductRepository productRepository;

    @Override
    public RefundResponse execute(String userId, RefundRequest request) {
        OrderEntity order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new BusinessException(OrderError.ORDER_NOT_FOUND));

        if (order.getUserId() == null || !order.getUserId().equals(userId)) {
            throw new BusinessException(RefundError.REFUND_NOT_ALLOWED, "Ban khong co quyen yeu cau hoan tien don hang nay");
        }

        ensureNoOpenRefund(order.getId());
        ensureOrderCanRequestAfterSale(order);

        PaymentEntity payment = resolvePayment(order);
        BigDecimal refundAmount = request.getRefundAmount() == null ? order.getTotalAmount() : request.getRefundAmount();
        validateRefundAmount(refundAmount, order.getTotalAmount());
        ExchangeSelection exchangeSelection = validateExchangeRequest(order, request);

        LocalDateTime now = LocalDateTime.now();
        RefundEntity refund = RefundEntity.builder()
                .id(UUID.randomUUID().toString())
                .orderId(order.getId())
                .paymentId(payment.getId())
                .userId(userId)
                .type(request.getType() == null ? RefundType.RETURN : request.getType())
                .refundAmount(refundAmount)
                .reason(request.getReason())
                .imageUrls(request.getImageUrls())
                .productId(exchangeSelection.orderItem() == null ? null : exchangeSelection.orderItem().getProductId())
                .productName(exchangeSelection.orderItem() == null ? null : exchangeSelection.orderItem().getProductName())
                .colorId(exchangeSelection.orderItem() == null ? null : exchangeSelection.orderItem().getColorId())
                .colorName(exchangeSelection.orderItem() == null ? null : exchangeSelection.orderItem().getColorName())
                .currentSizeId(exchangeSelection.orderItem() == null ? null : exchangeSelection.orderItem().getSizeId())
                .currentSizeName(exchangeSelection.orderItem() == null ? null : exchangeSelection.orderItem().getSizeName())
                .requestedSizeId(exchangeSelection.requestedSize() == null ? null : exchangeSelection.requestedSize().getId())
                .requestedSizeName(exchangeSelection.requestedSize() == null ? null : exchangeSelection.requestedSize().getSize())
                .bankCode(request.getBankCode())
                .bankName(request.getBankName())
                .accountNumber(request.getAccountNumber())
                .accountName(request.getAccountName())
                .transferContent("HOAN TIEN " + order.getOrderCode())
                .provider(payment.getProvider())
                .status(RefundStatus.PENDING)
                .requestedAt(now)
                .createdAt(now)
                .updatedAt(now)
                .build();

        order.setStatus(OrderStatus.RETURN_REQUESTED);
        order.setUpdatedAt(now);
        orderRepository.save(order);

        return RefundResponseMapper.toResponse(refundRepository.save(refund));
    }

    private void ensureNoOpenRefund(String orderId) {
        boolean exists = refundRepository.findByOrderId(orderId).stream()
                .anyMatch(refund -> OPEN_STATUSES.contains(refund.getStatus()));
        if (exists) {
            throw new BusinessException(RefundError.REFUND_ALREADY_EXISTS);
        }
    }

    private void ensureOrderCanRequestAfterSale(OrderEntity order) {
        if (order.getStatus() == null
                || order.getStatus().name().startsWith("RETURN")
                || order.getStatus().name().startsWith("REFUND")
                || order.getStatus() == x10.Clothing.api.common.domain.enums.OrderStatus.PENDING
                || order.getStatus() == x10.Clothing.api.common.domain.enums.OrderStatus.CANCELLED) {
            throw new BusinessException(RefundError.REFUND_NOT_ALLOWED, "Don hang khong o trang thai co the gui yeu cau tra/doi hang");
        }
    }

    private PaymentEntity resolvePayment(OrderEntity order) {
        return paymentRepository.findByOrderId(order.getId())
                .orElseGet(() -> paymentRepository.save(PaymentEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .orderId(order.getId())
                        .orderCode(order.getOrderCode())
                        .method(order.getPaymentMethod())
                        .status(order.getPaymentStatus())
                        .amount(order.getTotalAmount())
                        .currency("VND")
                        .provider(order.getPaymentMethod() == PaymentMethod.PAYOS ? "PAYOS" : "COD")
                        .providerOrderCode(order.getPayosOrderCode())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()));
    }

    private void validateRefundAmount(BigDecimal refundAmount, BigDecimal orderTotal) {
        if (refundAmount == null
                || refundAmount.compareTo(BigDecimal.ZERO) <= 0
                || orderTotal == null
                || refundAmount.compareTo(orderTotal) > 0) {
            throw new BusinessException(RefundError.INVALID_REFUND_DATA, "So tien hoan khong hop le");
        }
    }

    private ExchangeSelection validateExchangeRequest(OrderEntity order, RefundRequest request) {
        if (request.getType() != RefundType.EXCHANGE_SIZE) {
            return new ExchangeSelection(null, null);
        }

        if (request.getProductId() == null || request.getProductId().isBlank()
                || request.getCurrentSizeId() == null || request.getCurrentSizeId().isBlank()
                || request.getRequestedSizeId() == null || request.getRequestedSizeId().isBlank()) {
            throw new BusinessException(RefundError.INVALID_REFUND_DATA, "Doi hang can co productId, currentSizeId va requestedSizeId");
        }

        OrderItem orderItem = order.getItems().stream()
                .filter(item -> request.getProductId().equals(item.getProductId())
                        && request.getCurrentSizeId().equals(item.getSizeId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(RefundError.REFUND_NOT_ALLOWED, "Chi duoc doi size cua san pham da mua trong don hang"));

        if (request.getRequestedSizeId().equals(orderItem.getSizeId())) {
            throw new BusinessException(RefundError.INVALID_REFUND_DATA, "Size muon doi phai khac size hien tai");
        }

        ProductEntity product = productRepository.findById(orderItem.getProductId())
                .orElseThrow(() -> new BusinessException(RefundError.INVALID_REFUND_DATA, "Khong tim thay san pham can doi"));

        ColorVariantEntity color = product.getColors().stream()
                .filter(item -> orderItem.getColorId().equals(item.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(RefundError.REFUND_NOT_ALLOWED, "Chi duoc doi size trong cung mau/cung san pham"));

        SizeVariantEntity requestedSize = color.getSizes().stream()
                .filter(size -> request.getRequestedSizeId().equals(size.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(RefundError.REFUND_NOT_ALLOWED, "Size muon doi khong thuoc cung san pham"));

        return new ExchangeSelection(orderItem, requestedSize);
    }

    private record ExchangeSelection(OrderItem orderItem, SizeVariantEntity requestedSize) {
    }
}
