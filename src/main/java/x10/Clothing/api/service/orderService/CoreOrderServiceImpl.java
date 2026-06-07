package x10.Clothing.api.service.orderService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.service.orderService.createOrderUc.CreateOrderRequest;
import x10.Clothing.api.service.orderService.createOrderUc.ICreateOrderUc;
import x10.Clothing.api.service.orderService.deleteOrderUc.IDeleteOrderUc;
import x10.Clothing.api.service.orderService.getOrderUc.IGetAllOrdersUc;
import x10.Clothing.api.service.orderService.getOrderUc.IGetOrderUc;
import x10.Clothing.api.service.orderService.getOrderUc.IGetOrdersByUserIdUc;
import x10.Clothing.api.service.orderService.updateOrderUc.IUpdateOrderUc;
import x10.Clothing.api.service.orderService.updateOrderUc.UpdateOrderRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreOrderServiceImpl implements ICoreOrderService {

    private final ICreateOrderUc createOrderUc;
    private final IGetAllOrdersUc getAllOrdersUc;
    private final IGetOrdersByUserIdUc getOrdersByUserIdUc;
    private final IGetOrderUc getOrderUc;
    private final IUpdateOrderUc updateOrderUc;
    private final IDeleteOrderUc deleteOrderUc;

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {
        return createOrderUc.execute(request);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return getAllOrdersUc.execute();
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(String userId) {
        return getOrdersByUserIdUc.execute(userId);
    }

    @Override
    public OrderResponse getOrder(String orderId) {
        return getOrderUc.execute(orderId);
    }

    @Override
    public OrderResponse updateOrder(String orderId, UpdateOrderRequest request) {
        return updateOrderUc.execute(orderId, request);
    }

    @Override
    public void deleteOrder(String orderId) {
        deleteOrderUc.execute(orderId);
    }
}
