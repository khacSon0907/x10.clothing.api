package x10.Clothing.api.service.orderService.updateOrderUc;

import x10.Clothing.api.service.orderService.OrderResponse;

public interface IUpdateOrderUc {
    OrderResponse execute(String orderId, UpdateOrderRequest request);
}
