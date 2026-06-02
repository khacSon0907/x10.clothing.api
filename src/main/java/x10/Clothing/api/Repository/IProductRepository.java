package x10.Clothing.api.Repository;

import x10.Clothing.api.common.domain.entities.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface IProductRepository {
    List<ProductEntity> findAll();
    ProductEntity save(ProductEntity product);
    Optional<ProductEntity> findById(String id);
    Optional<ProductEntity> findBySlug(String slug);
    boolean existsBySlug(String slug);
    List<ProductEntity> findByNameContainingIgnoreCase(String name);
    void deleteById(String id);
}
