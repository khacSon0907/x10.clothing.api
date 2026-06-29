package x10.Clothing.api.service.refundService.rejectRefundUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IOrderRepository;
import x10.Clothing.api.Repository.IRefundRepository;
import x10.Clothing.api.common.domain.entities.order.OrderEntity;
import x10.Clothing.api.common.domain.entities.order.RefundEntity;
import x10.Clothing.api.common.domain.enums.OrderStatus;
import x10.Clothing.api.common.domain.enums.RefundStatus;
import x10.Clothing.api.service.refundService.RefundDecisionRequest;
import x10.Clothing.api.service.refundService.RefundResponse;
import x10.Clothing.api.service.refundService.RefundResponseMapper;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.order.OrderError;
import x10.Clothing.api.share.exception.refund.RefundError;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RejectRefundUcImpl implements IRejectRefundUc {

    private final IRefundRepository refundRepository;
    private final IOrderRepository orderRepository;

    @Override
    public RefundResponse execute(String refundId, RefundDecisionRequest request) {
        RefundEntity refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new BusinessException(RefundError.REFUND_NOT_FOUND));

        if (refund.getStatus() == RefundStatus.SUCCESS
                || refund.getStatus() == RefundStatus.EXCHANGED
                || refund.getStatus() == RefundStatus.REJECTED) {
            throw new BusinessException(RefundError.REFUND_NOT_ALLOWED, "Khong the tu choi yeu cau da hoan tat hoac da tu choi");
        }

        OrderEntity order = orderRepository.findById(refund.getOrderId())
                .orElseThrow(() -> new BusinessException(OrderError.ORDER_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();
        refund.setStatus(RefundStatus.REJECTED);
        refund.setRejectedReason(request == null ? null : request.getReason());
        refund.setAdminNote(request == null ? null : request.getNote());
        refund.setRejectedAt(now);
        refund.setUpdatedAt(now);

        order.setStatus(OrderStatus.CONFIRMED);
        order.setUpdatedAt(now);
        orderRepository.save(order);

        return RefundResponseMapper.toResponse(refundRepository.save(refund));
    }
}
