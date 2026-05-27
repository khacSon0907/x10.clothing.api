package x10.Clothing.api.common.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ProductEntity {

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
