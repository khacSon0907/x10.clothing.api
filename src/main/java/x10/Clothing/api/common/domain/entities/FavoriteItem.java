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
public class FavoriteItem {

    private String productId;

    private String productName;

    private String productImage;

    private BigDecimal price;
}