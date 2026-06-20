package x10.Clothing.api.service.orderService.getOrderUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.Clothing.api.service.orderService.OrderResponse;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCursorPageResponse {

    private List<OrderResponse> items;

    private String nextCursor;

    private boolean hasNext;

    private int limit;
}
