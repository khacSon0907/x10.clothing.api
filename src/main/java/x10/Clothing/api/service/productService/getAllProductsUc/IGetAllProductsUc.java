package x10.Clothing.api.service.productService.getAllProductsUc;

import x10.Clothing.api.common.domain.entities.product.ProductEntity;
import java.util.List;

public interface IGetAllProductsUc {
    List<ProductEntity> getAllProducts();

    ProductCursorPageResponse getAllProductsByCursor(String cursor, Integer limit);
}
