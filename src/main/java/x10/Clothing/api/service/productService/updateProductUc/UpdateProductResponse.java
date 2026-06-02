package x10.Clothing.api.service.productService.updateProductUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.Clothing.api.common.domain.entities.ColorVariantEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductResponse {
    private String id;
    private String name;
    private String slug;
    private String categoryId;
    private String description;
    private BigDecimal price;
    private BigDecimal salePrice;
    private Boolean active;
    private List<ColorVariantEntity> colors;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
