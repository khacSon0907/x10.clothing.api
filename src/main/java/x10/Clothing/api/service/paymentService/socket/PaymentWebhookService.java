package x10.Clothing.api.service.paymentService.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.model.webhooks.Webhook;
import vn.payos.model.webhooks.WebhookData;
import x10.Clothing.api.Repository.IOrderRepository;
import x10.Clothing.api.common.domain.entities.OrderEntity;
import x10.Clothing.api.common.domain.enums.OrderStatus;
import x10.Clothing.api.common.domain.enums.PaymentStatus;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentWebhookService {

    private final IOrderRepository orderRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final PayOS payOS;

    public void handleWebhook(Webhook webhook) {
        WebhookData data = payOS.webhooks().verify(webhook);
        Long payosOrderCode = data.getOrderCode();

        OrderEntity order = orderRepository.findByPayosOrderCode(payosOrderCode)
                .orElseThrow(() -> new RuntimeException("Order not found by payOS orderCode"));

        PaymentStatus paymentStatus = "00".equals(data.getCode())
                ? PaymentStatus.PAID
                : PaymentStatus.FAILED;

        order.setPaymentStatus(paymentStatus);
        if (PaymentStatus.PAID == paymentStatus) {
            order.setStatus(OrderStatus.CONFIRMED);
        }
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);

        PaymentSocketMessage message = PaymentSocketMessage.builder()
                .orderId(order.getId())
                .orderCode(payosOrderCode)
                .paymentStatus(paymentStatus)
                .message(PaymentStatus.PAID == paymentStatus ? "Thanh toan thanh cong" : "Thanh toan that bai")
                .build();

        messagingTemplate.convertAndSend(
                "/topic/payment/" + order.getId(),
                message
        );
    }
}
