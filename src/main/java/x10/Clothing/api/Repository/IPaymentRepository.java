package x10.Clothing.api.Repository;

import x10.Clothing.api.common.domain.entities.order.PaymentEntity;

import java.util.List;
import java.util.Optional;

public interface IPaymentRepository {

    PaymentEntity save(PaymentEntity payment);

    Optional<PaymentEntity> findById(String id);

    Optional<PaymentEntity> findByOrderId(String orderId);

    Optional<PaymentEntity> findByProviderOrderCode(Long providerOrderCode);

    List<PaymentEntity> findAll();
}
