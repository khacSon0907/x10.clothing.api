package x10.Clothing.api.infrastructure.refund.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import x10.Clothing.api.common.domain.enums.RefundStatus;

import java.util.List;

public interface RefundMongoRepository extends MongoRepository<RefundDocument, String> {

    List<RefundDocument> findAllByOrderByCreatedAtDesc();

    List<RefundDocument> findByUserIdOrderByCreatedAtDesc(String userId);

    List<RefundDocument> findByOrderIdOrderByCreatedAtDesc(String orderId);

    List<RefundDocument> findByStatusOrderByCreatedAtDesc(RefundStatus status);
}
