package x10.Clothing.api.service.refundService;

import java.util.List;

public interface ICoreRefundService {

    RefundResponse requestRefund(String userId, RefundRequest request);

    List<RefundResponse> getMyRefunds(String userId);

    List<RefundResponse> getAllRefunds();

    RefundResponse approveRefund(String refundId, RefundDecisionRequest request);

    RefundResponse markReturnReceived(String refundId, RefundDecisionRequest request);

    RefundResponse markRefunded(String refundId, RefundDecisionRequest request);

    RefundResponse rejectRefund(String refundId, RefundDecisionRequest request);
}
