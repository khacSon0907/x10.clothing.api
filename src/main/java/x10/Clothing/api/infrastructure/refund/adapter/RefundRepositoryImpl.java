package x10.Clothing.api.infrastructure.refund.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.Clothing.api.Repository.IRefundRepository;
import x10.Clothing.api.common.domain.entities.order.RefundEntity;
import x10.Clothing.api.common.domain.enums.RefundStatus;
import x10.Clothing.api.infrastructure.refund.db.mongodb.RefundMongoRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefundRepositoryImpl implements IRefundRepository {

    private final RefundMongoRepository refundMongoRepository;

    @Override
    public RefundEntity save(RefundEntity refund) {
        return RefundMapper.toEntity(refundMongoRepository.save(RefundMapper.toDocument(refund)));
    }

    @Override
    public Optional<RefundEntity> findById(String id) {
        return refundMongoRepository.findById(id).map(RefundMapper::toEntity);
    }

    @Override
    public List<RefundEntity> findAll() {
        return refundMongoRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(RefundMapper::toEntity)
                .toList();
    }

    @Override
    public List<RefundEntity> findByUserId(String userId) {
        return refundMongoRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(RefundMapper::toEntity)
                .toList();
    }

    @Override
    public List<RefundEntity> findByOrderId(String orderId) {
        return refundMongoRepository.findByOrderIdOrderByCreatedAtDesc(orderId).stream()
                .map(RefundMapper::toEntity)
                .toList();
    }

    @Override
    public List<RefundEntity> findByStatus(RefundStatus status) {
        return refundMongoRepository.findByStatusOrderByCreatedAtDesc(status).stream()
                .map(RefundMapper::toEntity)
                .toList();
    }
}
