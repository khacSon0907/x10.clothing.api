package x10.Clothing.api.service.productService.createProductUc;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import x10.Clothing.api.Repository.IProductRepository;
import x10.Clothing.api.common.domain.entities.ColorVariantEntity;
import x10.Clothing.api.common.domain.entities.ProductEntity;
import x10.Clothing.api.common.domain.entities.ProductImageEntity;
import x10.Clothing.api.common.domain.entities.SizeVariantEntity;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.product.ProductError;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CreateProductUcImpl implements ICreateProductUc {

    private final IProductRepository productRepository;

    @Override
    public CreateProductResponse createProduct(CreateProductRequest request) {
        
        String slug = request.getSlug();
        if (slug == null || slug.trim().isEmpty()) {
            slug = generateSlug(request.getName());
        }

        if (productRepository.existsBySlug(slug)) {
            throw new BusinessException(ProductError.PRODUCT_ALREADY_EXISTS);
        }

        List<ColorVariantEntity> colors = Collections.emptyList();
        if (request.getColors() != null) {
            colors = request.getColors().stream().map(colorDto -> {
                List<ProductImageEntity> images = Collections.emptyList();
                if (colorDto.getImages() != null) {
                    images = colorDto.getImages().stream().map(imgDto ->
                            ProductImageEntity.builder()
                                    .id(UUID.randomUUID().toString())
                                    .url(imgDto.getUrl())
                                    .publicId(imgDto.getPublicId())
                                    .main(imgDto.getMain() != null ? imgDto.getMain() : false)
                                    .sortOrder(imgDto.getSortOrder())
                                    .build()
                    ).collect(Collectors.toList());
                }

                List<SizeVariantEntity> sizes = Collections.emptyList();
                if (colorDto.getSizes() != null) {
                    sizes = colorDto.getSizes().stream().map(sizeDto ->
                            SizeVariantEntity.builder()
                                    .id(UUID.randomUUID().toString())
                                    .size(sizeDto.getSize())
                                    .sku(sizeDto.getSku())
                                    .quantity(sizeDto.getQuantity() != null ? sizeDto.getQuantity() : 0)
                                    .build()
                    ).collect(Collectors.toList());
                }

                return ColorVariantEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .colorName(colorDto.getColorName())
                        .colorCode(colorDto.getColorCode())
                        .images(images)
                        .sizes(sizes)
                        .build();
            }).collect(Collectors.toList());
        }

        ProductEntity product = ProductEntity.builder()
                .name(request.getName())
                .slug(slug)
                .categoryId(request.getCategoryId())
                .description(request.getDescription())
                .price(request.getPrice())
                .salePrice(request.getSalePrice())
                .active(request.getActive() != null ? request.getActive() : true)
                .colors(colors)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ProductEntity savedProduct = productRepository.save(product);

        return CreateProductResponse.builder()
                .id(savedProduct.getId())
                .name(savedProduct.getName())
                .slug(savedProduct.getSlug())
                .categoryId(savedProduct.getCategoryId())
                .description(savedProduct.getDescription())
                .price(savedProduct.getPrice())
                .salePrice(savedProduct.getSalePrice())
                .active(savedProduct.getActive())
                .colors(savedProduct.getColors())
                .createdAt(savedProduct.getCreatedAt())
                .updatedAt(savedProduct.getUpdatedAt())
                .build();
    }

    private String generateSlug(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String noDiacritics = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return noDiacritics.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
    }
}
