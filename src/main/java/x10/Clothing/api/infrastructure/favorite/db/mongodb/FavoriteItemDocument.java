package x10.Clothing.api.infrastructure.favorite.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteItemDocument {

    private String productId;

    private String productName;

    private String productImage;

    private String colorId;

    private String colorName;

    private String sizeId;

    private String sizeName;

    private Integer quantity;

    private BigDecimal unitPrice;
}

