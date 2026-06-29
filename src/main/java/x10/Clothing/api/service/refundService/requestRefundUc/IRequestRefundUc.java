package x10.Clothing.api.service.refundService.requestRefundUc;

import x10.Clothing.api.service.refundService.RefundRequest;
import x10.Clothing.api.service.refundService.RefundResponse;

public interface IRequestRefundUc {

    RefundResponse execute(String userId, RefundRequest request);
}
