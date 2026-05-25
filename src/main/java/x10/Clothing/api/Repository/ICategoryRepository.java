package x10.Clothing.api.Repository;

import x10.Clothing.api.common.domain.entities.CategoryEntity;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepository {
    CategoryEntity save(CategoryEntity category);
    Optional<CategoryEntity> findBySlug(String slug);
    Optional<CategoryEntity> findByName(String name);
    List<CategoryEntity> findAll();
}
