package x10.Clothing.api.infrastructure.product.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.Clothing.api.Repository.IProductRepository;
import x10.Clothing.api.common.domain.entities.ProductEntity;
import x10.Clothing.api.infrastructure.product.adapter.mapper.ProductMapper;
import x10.Clothing.api.infrastructure.product.db.mongodb.ProductDocument;
import x10.Clothing.api.infrastructure.product.db.mongodb.ProductMongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements IProductRepository {

    private final ProductMongoRepository productMongoRepository;

    @Override
    public ProductEntity save(ProductEntity product) {
        ProductDocument document = ProductMapper.toDocument(product);
        ProductDocument savedDocument = productMongoRepository.save(document);
        return ProductMapper.toEntity(savedDocument);
    }

    @Override
    public List<ProductEntity> findAll() {
        return productMongoRepository.findAll().stream()
                .map(ProductMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductEntity> findById(String id) {
        return productMongoRepository.findById(id)
                .map(ProductMapper::toEntity);
    }

    @Override
    public Optional<ProductEntity> findBySlug(String slug) {
        return productMongoRepository.findBySlug(slug)
                .map(ProductMapper::toEntity);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return productMongoRepository.existsBySlug(slug);
    }
}
