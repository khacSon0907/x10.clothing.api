package x10.Clothing.api.service.refundService.getRefundUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.Clothing.api.service.refundService.RefundResponse;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundCursorPageResponse {

    private List<RefundResponse> items;

    private String nextCursor;

    private boolean hasNext;

    private int limit;
}
