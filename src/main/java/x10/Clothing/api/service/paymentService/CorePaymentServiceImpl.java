package x10.Clothing.api.service.paymentService;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.service.paymentService.createPayment.CreatePaymentLinkRequest;
import x10.Clothing.api.service.paymentService.createPayment.CreatePaymentLinkResponse;
import x10.Clothing.api.service.paymentService.createPayment.ICreatePaymentLinkUc;

@Service
@RequiredArgsConstructor
public class CorePaymentServiceImpl implements ICorePaymentService {

    private final ICreatePaymentLinkUc createPaymentLinkUc;

    @Override
    public CreatePaymentLinkResponse createPaymentLink(CreatePaymentLinkRequest request) {
        return createPaymentLinkUc.createPaymentLink(request);
    }
}