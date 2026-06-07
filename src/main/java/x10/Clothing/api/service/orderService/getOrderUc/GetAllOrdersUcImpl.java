package x10.Clothing.api.service.orderService.getOrderUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IOrderRepository;
import x10.Clothing.api.service.orderService.OrderResponse;
import x10.Clothing.api.service.orderService.OrderResponseMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetAllOrdersUcImpl implements IGetAllOrdersUc {

    private final IOrderRepository orderRepository;

    @Override
    public List<OrderResponse> execute() {
        return orderRepository.findAll().stream()
                .map(OrderResponseMapper::toResponse)
                .collect(Collectors.toList());
    }
}
