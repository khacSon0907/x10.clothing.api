package x10.Clothing.api.service.productService.getProductsByCategoryIdUc;

import x10.Clothing.api.common.domain.entities.ProductEntity;

import java.util.List;

public interface IGetProductsByCategoryIdUc {
    List<ProductEntity> getProductsByCategoryId(String categoryId);
}

