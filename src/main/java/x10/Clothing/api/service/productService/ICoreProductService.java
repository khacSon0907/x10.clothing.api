package x10.Clothing.api.service.productService;

import x10.Clothing.api.common.domain.entities.ProductEntity;
import x10.Clothing.api.service.productService.createProductUc.CreateProductRequest;
import x10.Clothing.api.service.productService.createProductUc.CreateProductResponse;

import java.util.List;

public interface ICoreProductService {
    CreateProductResponse createProduct(CreateProductRequest request);
    List<ProductEntity> getAllProducts();
}
