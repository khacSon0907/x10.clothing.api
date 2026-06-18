package x10.Clothing.api.service.productService.getProductUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.Clothing.api.Repository.IProductRepository;
import x10.Clothing.api.common.domain.entities.product.ProductEntity;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.product.ProductError;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GetProductUcImpl implements IGetProductUc {

    private final IProductRepository productRepository;

    @Override
    public ProductEntity getProductByIdOrSlug(String idOrSlug) {
        if (idOrSlug == null || idOrSlug.isBlank()) {
            throw new IllegalArgumentException("ID hoặc Slug là bắt buộc");
        }

        Optional<ProductEntity> productOpt = productRepository.findById(idOrSlug);
        
        if (productOpt.isEmpty()) {
            productOpt = productRepository.findBySlug(idOrSlug);
        }

        return productOpt.orElseThrow(() -> new BusinessException(ProductError.PRODUCT_NOT_FOUND));
    }
}
