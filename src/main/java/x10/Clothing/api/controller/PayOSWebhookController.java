package x10.Clothing.api.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.payos.model.webhooks.Webhook;
import x10.Clothing.api.service.paymentService.socket.PaymentWebhookService;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PayOSWebhookController {

    private final PaymentWebhookService paymentWebhookService;

    @PostMapping("/payos-webhook")
    public String handlePayOSWebhook(@RequestBody Webhook webhook) {


        System.out.println("===== PAYOS WEBHOOK =====");
        System.out.println(webhook);
        paymentWebhookService.handleWebhook(webhook);
        return "OK";
    }
}
