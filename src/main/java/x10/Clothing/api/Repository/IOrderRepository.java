package x10.Clothing.api.Repository;

import x10.Clothing.api.common.domain.entities.order.OrderEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IOrderRepository {

    OrderEntity save(OrderEntity order);

    List<OrderEntity> findAll();

    List<OrderEntity> findAllByCursor(LocalDateTime cursorCreatedAt, String cursorId, int limit);

    List<OrderEntity> findByUserId(String userId);

    List<OrderEntity> findByUserIdByCursor(String userId, LocalDateTime cursorCreatedAt, String cursorId, int limit);

    Optional<OrderEntity> findById(String id);

    Optional<OrderEntity> findByOrderCode(String orderCode);

    Optional<OrderEntity> findByPayosOrderCode(Long payosOrderCode);

    void deleteById(String id);
}
