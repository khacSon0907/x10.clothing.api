package x10.Clothing.api.service.orderService.deleteOrderUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IOrderRepository;
import x10.Clothing.api.common.domain.entities.order.OrderEntity;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.order.OrderError;

@Service
@RequiredArgsConstructor
public class DeleteOrderUcImpl implements IDeleteOrderUc {

    private final IOrderRepository orderRepository;

    @Override
    public void execute(String orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .or(() -> orderRepository.findByOrderCode(orderId))
                .orElseThrow(() -> new BusinessException(OrderError.ORDER_NOT_FOUND));

        orderRepository.deleteById(order.getId());
    }
}
