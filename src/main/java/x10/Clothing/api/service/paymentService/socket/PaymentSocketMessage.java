package x10.Clothing.api.service.paymentService.socket;

import lombok.Builder;
import lombok.Data;
import x10.Clothing.api.common.domain.enums.PaymentStatus;

@Data
@Builder
public class PaymentSocketMessage {

    private String orderId;
    private Long orderCode;
    private PaymentStatus paymentStatus;
    private String message;
}
