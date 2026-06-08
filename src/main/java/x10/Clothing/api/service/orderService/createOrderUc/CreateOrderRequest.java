package x10.Clothing.api.service.orderService.createOrderUc;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateOrderRequest {

    @NotBlank(message = "Nguoi dung khong duoc de trong")
    private String userId;

    @NotBlank(message = "Ten nguoi nhan khong duoc de trong")
    private String receiverName;

    @NotBlank(message = "So dien thoai nguoi nhan khong duoc de trong")
    private String receiverPhone;

    @NotBlank(message = "Dia chi nguoi nhan khong duoc de trong")
    private String receiverAddress;

    @Valid
    @NotEmpty(message = "Don hang phai co san pham")
    private List<OrderItemRequest> items;

    @DecimalMin(value = "0.0", message = "Phi van chuyen khong duoc am")
    private BigDecimal shippingFee = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "So tien giam gia khong duoc am")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Pattern(regexp = "(?i)COD|PAYOS|MOMO", message = "Phuong thuc thanh toan khong hop le")
    private String paymentMethod = "COD";

    private String note;

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
