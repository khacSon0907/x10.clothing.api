package x10.Clothing.api.infrastructure.category.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryMongoRepository extends MongoRepository<CategoryDocument, String> {
    Optional<CategoryDocument> findBySlug(String slug);
    Optional<CategoryDocument> findByName(String name);
}

