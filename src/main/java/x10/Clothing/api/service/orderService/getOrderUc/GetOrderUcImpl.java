package x10.Clothing.api.service.orderService.getOrderUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IOrderRepository;
import x10.Clothing.api.common.domain.entities.OrderEntity;
import x10.Clothing.api.service.orderService.OrderResponse;
import x10.Clothing.api.service.orderService.OrderResponseMapper;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.order.OrderError;

@Service
@RequiredArgsConstructor
public class GetOrderUcImpl implements IGetOrderUc {

    private final IOrderRepository orderRepository;

    @Override
    public OrderResponse execute(String orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .or(() -> orderRepository.findByOrderCode(orderId))
                .orElseThrow(() -> new BusinessException(OrderError.ORDER_NOT_FOUND));

        return OrderResponseMapper.toResponse(order);
    }
}
