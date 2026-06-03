package x10.Clothing.api.common.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private String productId;

    private String colorId;

    private String sizeId;

    private Integer quantity;

    private BigDecimal unitPrice;
}