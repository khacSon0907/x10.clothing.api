package x10.Clothing.api.service.orderService.createOrderUc;

import x10.Clothing.api.service.orderService.OrderResponse;

public interface ICreateOrderUc {
    OrderResponse execute(CreateOrderRequest request);
}
