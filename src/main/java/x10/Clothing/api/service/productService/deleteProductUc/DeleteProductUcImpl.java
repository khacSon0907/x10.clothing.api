package x10.Clothing.api.service.productService.deleteProductUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.Clothing.api.Repository.IProductRepository;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.product.ProductError;

@Component
@RequiredArgsConstructor
public class DeleteProductUcImpl implements IDeleteProductUc {

    private final IProductRepository productRepository;

    @Override
    public void deleteProduct(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Product ID là bắt buộc");
        }
        productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ProductError.PRODUCT_NOT_FOUND));
        productRepository.deleteById(id);
    }
}
