package x10.Clothing.api.service.refundService.approveRefundUc;

import x10.Clothing.api.service.refundService.RefundDecisionRequest;
import x10.Clothing.api.service.refundService.RefundResponse;

public interface IApproveRefundUc {

    RefundResponse execute(String refundId, RefundDecisionRequest request);
}
