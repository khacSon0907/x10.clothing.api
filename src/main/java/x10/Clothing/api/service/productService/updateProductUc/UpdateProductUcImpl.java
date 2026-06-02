package x10.Clothing.api.service.productService.updateProductUc;

import lombok.RequiredArgsConstructor;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UpdateProductUcImpl implements IUpdateProductUc {

    private final IProductRepository productRepository;

    @Override
    public UpdateProductResponse updateProduct(UpdateProductRequest request) {
        ProductEntity existing = productRepository.findById(request.getId())
                .orElseThrow(() -> new BusinessException(ProductError.PRODUCT_NOT_FOUND));

        if (request.getName() != null && !request.getName().isBlank()) {
            existing.setName(request.getName().trim());
        }

        String slug = request.getSlug();
        if (slug != null && !slug.isBlank()) {
            slug = slug.trim();
        } else if (request.getName() != null && !request.getName().isBlank()) {
            slug = generateSlug(request.getName());
        }

        if (slug != null) {
            Optional<ProductEntity> withSlug = productRepository.findBySlug(slug);
            if (withSlug.isPresent() && !withSlug.get().getId().equals(existing.getId())) {
                throw new BusinessException(ProductError.PRODUCT_ALREADY_EXISTS);
            }
            existing.setSlug(slug);
        }

        if (request.getCategoryId() != null && !request.getCategoryId().isBlank()) {
            existing.setCategoryId(request.getCategoryId());
        }

        if (request.getDescription() != null) {
            existing.setDescription(request.getDescription());
        }

        if (request.getPrice() != null) {
            existing.setPrice(request.getPrice());
        }

        if (request.getSalePrice() != null) {
            existing.setSalePrice(request.getSalePrice());
        }

        if (request.getActive() != null) {
            existing.setActive(request.getActive());
        }

        if (request.getColors() != null) {
            List<ColorVariantEntity> colors = request.getColors().stream().map(colorDto -> {
                List<ProductImageEntity> images = Collections.emptyList();
                if (colorDto.getImages() != null) {
                    images = colorDto.getImages().stream().map(imgDto ->
                            ProductImageEntity.builder()
                                    .id(imgDto.getId() != null && !imgDto.getId().isBlank() ? imgDto.getId() : UUID.randomUUID().toString())
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
                                    .id(sizeDto.getId() != null && !sizeDto.getId().isBlank() ? sizeDto.getId() : UUID.randomUUID().toString())
                                    .size(sizeDto.getSize())
                                    .sku(sizeDto.getSku())
                                    .quantity(sizeDto.getQuantity() != null ? sizeDto.getQuantity() : 0)
                                    .build()
                    ).collect(Collectors.toList());
                }

                return ColorVariantEntity.builder()
                        .id(colorDto.getId() != null && !colorDto.getId().isBlank() ? colorDto.getId() : UUID.randomUUID().toString())
                        .colorName(colorDto.getColorName())
                        .colorCode(colorDto.getColorCode())
                        .images(images)
                        .sizes(sizes)
                        .build();
            }).collect(Collectors.toList());

            existing.setColors(colors);
        }

        existing.setUpdatedAt(LocalDateTime.now());

        ProductEntity saved = productRepository.save(existing);

        return UpdateProductResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .slug(saved.getSlug())
                .categoryId(saved.getCategoryId())
                .description(saved.getDescription())
                .price(saved.getPrice())
                .salePrice(saved.getSalePrice())
                .active(saved.getActive())
                .colors(saved.getColors())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    private String generateSlug(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String noDiacritics = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return noDiacritics.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
    }
}
