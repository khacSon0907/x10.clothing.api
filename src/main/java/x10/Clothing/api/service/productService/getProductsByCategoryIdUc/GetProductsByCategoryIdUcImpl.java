package x10.Clothing.api.service.productService.getProductsByCategoryIdUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.Clothing.api.Repository.IProductRepository;
import x10.Clothing.api.common.domain.entities.product.ProductEntity;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetProductsByCategoryIdUcImpl implements IGetProductsByCategoryIdUc {

    private final IProductRepository productRepository;

    @Override
    public List<ProductEntity> getProductsByCategoryId(String categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
}

