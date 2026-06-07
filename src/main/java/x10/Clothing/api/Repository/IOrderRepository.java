package x10.Clothing.api.Repository;

import x10.Clothing.api.common.domain.entities.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface IOrderRepository {

    OrderEntity save(OrderEntity order);

    List<OrderEntity> findAll();

    List<OrderEntity> findByUserId(String userId);

    Optional<OrderEntity> findById(String id);

    Optional<OrderEntity> findByOrderCode(String orderCode);

    void deleteById(String id);
}
