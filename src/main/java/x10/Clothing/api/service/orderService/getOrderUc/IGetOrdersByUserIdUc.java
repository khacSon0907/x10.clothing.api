package x10.Clothing.api.service.orderService.getOrderUc;

import x10.Clothing.api.service.orderService.OrderResponse;

import java.util.List;

public interface IGetOrdersByUserIdUc {
    List<OrderResponse> execute(String userId);
}
