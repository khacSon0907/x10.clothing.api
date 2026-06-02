package x10.Clothing.api.service.productService.getProductUc;

import x10.Clothing.api.common.domain.entities.ProductEntity;

public interface IGetProductUc {
    ProductEntity getProductByIdOrSlug(String idOrSlug);
}
