package x10.Clothing.api.infrastructure.banner.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BannerMongoRepository extends MongoRepository<BannerDocument, String> {
    List<BannerDocument> findByActiveTrue();
}
