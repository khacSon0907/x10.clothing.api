package x10.Clothing.api.common.domain.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    private String id;
    private String orderCode;

    private String userId;

    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;

    private List<OrderItem> items;

    private BigDecimal subtotal;
    private BigDecimal shippingFee;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;

    private String paymentMethod;
    private String paymentStatus;

    private String status;

    private String note;

    private String cancelReason;

    private String trackingCode;
    private String shippingProvider;



    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}