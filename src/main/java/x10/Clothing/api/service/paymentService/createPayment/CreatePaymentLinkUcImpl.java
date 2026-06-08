package x10.Clothing.api.service.paymentService.createPayment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IOrderRepository;
import x10.Clothing.api.common.domain.entities.OrderEntity;

@Service
@RequiredArgsConstructor
public class CreatePaymentLinkUcImpl implements ICreatePaymentLinkUc {

    private final IOrderRepository orderRepository;
    private final PayOSService payOSService;

    @Override
    public CreatePaymentLinkResponse createPaymentLink(
            CreatePaymentLinkRequest request
    ) {

        OrderEntity order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        Long orderCode = System.currentTimeMillis() / 1000;

        vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse payOSResponse =
                payOSService.createPaymentLink(
                        orderCode,
                        order.getTotalAmount().longValue(),
                        "DH" + orderCode
                );

        return CreatePaymentLinkResponse.builder()
                .orderId(order.getId())
                .orderCode(orderCode)
                .paymentLinkId(payOSResponse.getPaymentLinkId())
                .amount(order.getTotalAmount())
                .checkoutUrl(payOSResponse.getCheckoutUrl())
                .qrCode(payOSResponse.getQrCode())
                .build();
    }
}