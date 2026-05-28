package x10.Clothing.api.service.productService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.common.domain.entities.ProductEntity;
import x10.Clothing.api.service.productService.createProductUc.CreateProductRequest;
import x10.Clothing.api.service.productService.createProductUc.CreateProductResponse;
import x10.Clothing.api.service.productService.createProductUc.ICreateProductUc;
import x10.Clothing.api.service.productService.getAllProductsUc.IGetAllProductsUc;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreProductServiceImpl implements ICoreProductService {

    private final ICreateProductUc createProductUc;
    private final IGetAllProductsUc getAllProductsUc;

    @Override
    public CreateProductResponse createProduct(CreateProductRequest request) {
        return createProductUc.createProduct(request);
    }

    @Override
    public List<ProductEntity> getAllProducts() {
        return getAllProductsUc.getAllProducts();
    }
}
