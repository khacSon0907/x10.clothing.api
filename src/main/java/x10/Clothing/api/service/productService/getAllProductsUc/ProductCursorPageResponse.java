package x10.Clothing.api.service.productService.getAllProductsUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.Clothing.api.common.domain.entities.product.ProductEntity;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCursorPageResponse {

    private List<ProductEntity> items;

    private String nextCursor;

    private boolean hasNext;

    private int limit;
}
