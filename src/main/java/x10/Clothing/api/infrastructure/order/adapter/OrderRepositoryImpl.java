package x10.Clothing.api.infrastructure.order.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.Clothing.api.Repository.IOrderRepository;
import x10.Clothing.api.common.domain.entities.OrderEntity;
import x10.Clothing.api.infrastructure.order.db.mongodb.OrderDocument;
import x10.Clothing.api.infrastructure.order.db.mongodb.OrderMongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements IOrderRepository {

    private final OrderMongoRepository orderMongoRepository;

    @Override
    public OrderEntity save(OrderEntity order) {
        OrderDocument document = OrderMapper.toDocument(order);
        OrderDocument saved = orderMongoRepository.save(document);
        return OrderMapper.toEntity(saved);
    }

    @Override
    public List<OrderEntity> findAll() {
        return orderMongoRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(OrderMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderEntity> findByUserId(String userId) {
        return orderMongoRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(OrderMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderEntity> findById(String id) {
        return orderMongoRepository.findById(id)
                .map(OrderMapper::toEntity);
    }

    @Override
    public Optional<OrderEntity> findByOrderCode(String orderCode) {
        return orderMongoRepository.findByOrderCode(orderCode)
                .map(OrderMapper::toEntity);
    }

    @Override
    public Optional<OrderEntity> findByPayosOrderCode(Long payosOrderCode) {
        return orderMongoRepository.findByPayosOrderCode(payosOrderCode)
                .map(OrderMapper::toEntity);
    }

    @Override
    public void deleteById(String id) {
        orderMongoRepository.deleteById(id);
    }
}
