package x10.Clothing.api.service.orderService.updateOrderUc;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateOrderRequest {
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;

    @Valid
    private List<OrderItemRequest> items;

    @DecimalMin(value = "0.0", message = "Phi van chuyen khong duoc am")
    private BigDecimal shippingFee;

    @DecimalMin(value = "0.0", message = "So tien giam gia khong duoc am")
    private BigDecimal discountAmount;

    private String paymentMethod;
    private String paymentStatus;
    private String status;
    private String note;
    private String cancelReason;
    private String trackingCode;
    private String shippingProvider;

    @Data
    public static class OrderItemRequest {
        @NotBlank(message = "San pham khong duoc de trong")
        private String productId;
        private String productName;
        private String productImage;
        private String colorId;
        private String colorName;
        private String sizeId;
        private String sizeName;

        @NotNull(message = "So luong khong duoc de trong")
        @Min(value = 1, message = "So luong phai lon hon 0")
        private Integer quantity;
    }
}
