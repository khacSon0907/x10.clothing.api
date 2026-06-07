package x10.Clothing.api.service.orderService.getOrderUc;

import x10.Clothing.api.service.orderService.OrderResponse;

public interface IGetOrderUc {
    OrderResponse execute(String orderId);
}
