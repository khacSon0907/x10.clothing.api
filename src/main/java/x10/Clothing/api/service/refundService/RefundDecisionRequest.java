package x10.Clothing.api.service.refundService;

import lombok.Data;

@Data
public class RefundDecisionRequest {

    private String note;

    private String reason;
}
