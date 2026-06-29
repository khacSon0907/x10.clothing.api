package x10.Clothing.api.service.refundService.port;

public interface RefundPaymentGateway {

    String provider();

    RefundPaymentResult refund(RefundPaymentCommand command);
}
