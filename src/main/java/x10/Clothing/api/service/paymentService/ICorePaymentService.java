package x10.Clothing.api.service.paymentService;

import x10.Clothing.api.service.paymentService.createPayment.CreatePaymentLinkRequest;
import x10.Clothing.api.service.paymentService.createPayment.CreatePaymentLinkResponse;

public interface ICorePaymentService {
    CreatePaymentLinkResponse createPaymentLink(CreatePaymentLinkRequest request);
}
