package x10.Clothing.api.service.orderService;

import x10.Clothing.api.service.orderService.createOrderUc.CreateOrderRequest;
import x10.Clothing.api.service.orderService.updateOrderUc.UpdateOrderRequest;

import java.util.List;

public interface ICoreOrderService {
    OrderResponse createOrder(CreateOrderRequest request);
    List<OrderResponse> getAllOrders();
    List<OrderResponse> getOrdersByUserId(String userId);
    OrderResponse getOrder(String orderId);
    OrderResponse updateOrder(String orderId, UpdateOrderRequest request);
    void deleteOrder(String orderId);
}
