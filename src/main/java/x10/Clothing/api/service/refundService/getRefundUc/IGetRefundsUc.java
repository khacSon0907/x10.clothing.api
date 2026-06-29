package x10.Clothing.api.service.refundService.getRefundUc;

import x10.Clothing.api.service.refundService.RefundResponse;

import java.util.List;

public interface IGetRefundsUc {

    List<RefundResponse> getMyRefunds(String userId);

    List<RefundResponse> getAllRefunds();
}
