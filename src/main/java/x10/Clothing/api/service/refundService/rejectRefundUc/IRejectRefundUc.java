package x10.Clothing.api.service.refundService.rejectRefundUc;

import x10.Clothing.api.service.refundService.RefundDecisionRequest;
import x10.Clothing.api.service.refundService.RefundResponse;

public interface IRejectRefundUc {

    RefundResponse execute(String refundId, RefundDecisionRequest request);
}
