package x10.Clothing.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import x10.Clothing.api.common.domain.enums.PaymentStatus;
import x10.Clothing.api.service.paymentService.socket.PaymentSocketMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/send")
    public String send() {
        messagingTemplate.convertAndSend(
                "/topic/payment/123",
                PaymentSocketMessage.builder()
                        .orderId("123")
                        .orderCode(123L)
                        .paymentStatus(PaymentStatus.PAID)
                        .message("Thanh toan thanh cong")
                        .build()
        );

        return "OK";
    }
}
