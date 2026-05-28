package x10.Clothing.api.service.productService.getAllProductsUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.Clothing.api.Repository.IProductRepository;
import x10.Clothing.api.common.domain.entities.ProductEntity;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllProductsUcImpl implements IGetAllProductsUc {

    private final IProductRepository productRepository;

    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }
}
