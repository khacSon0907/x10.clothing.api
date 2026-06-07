package x10.Clothing.api.infrastructure.order.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrderMongoRepository extends MongoRepository<OrderDocument, String> {
    List<OrderDocument> findAllByOrderByCreatedAtDesc();
    List<OrderDocument> findByUserIdOrderByCreatedAtDesc(String userId);
    Optional<OrderDocument> findByOrderCode(String orderCode);
}
