package x10.Clothing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import x10.Clothing.api.service.paymentService.ICorePaymentService;
import x10.Clothing.api.service.paymentService.createPayment.CreatePaymentLinkRequest;
import x10.Clothing.api.service.paymentService.createPayment.CreatePaymentLinkResponse;
import x10.Clothing.api.share.response.ApiResponse;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final ICorePaymentService paymentService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CreatePaymentLinkResponse> createPaymentLink(
            @Valid @RequestBody CreatePaymentLinkRequest request,
            HttpServletRequest httpRequest
    ) {
        CreatePaymentLinkResponse response = paymentService.createPaymentLink(request);
        return ApiResponse.success(
                201,
                "PAYMENT.CREATE_SUCCESS",
                "Tao link thanh toan thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }
}