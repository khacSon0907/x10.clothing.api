package x10.Clothing.api.service.productService.searchProductByNameUc;

import x10.Clothing.api.common.domain.entities.product.ProductEntity;

import java.util.List;

public interface ISearchProductByNameUc {
    List<ProductEntity> searchProductByName(String name);
}
