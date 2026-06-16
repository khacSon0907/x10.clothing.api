package x10.Clothing.api.infrastructure.mail.template;

import x10.Clothing.api.common.domain.entities.OrderEntity;
import x10.Clothing.api.common.domain.entities.OrderItem;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.stream.Collectors;

public class OrderInvoiceEmailTemplate {

    private static final Locale VIETNAM = Locale.forLanguageTag("vi-VN");

    private OrderInvoiceEmailTemplate() {
    }

    public static String build(String username, OrderEntity order) {
        String customerName = username == null || username.isBlank()
                ? order.getReceiverName()
                : username;

        String items = order.getItems().stream()
                .map(OrderInvoiceEmailTemplate::formatItem)
                .collect(Collectors.joining("\n"));

        return """
                Xin chao %s,

                X10 Clothing da nhan don hang cua ban.

                Ma don hang: %s
                Nguoi nhan: %s
                So dien thoai: %s
                Dia chi: %s

                San pham:
                %s

                Tam tinh: %s
                Phi van chuyen: %s
                Giam gia: %s
                Tong thanh toan: %s
                Phuong thuc thanh toan: %s
                Trang thai thanh toan: %s

                Cam on ban da mua hang tai X10 Clothing.
                """.formatted(
                customerName,
                order.getOrderCode(),
                order.getReceiverName(),
                order.getReceiverPhone(),
                order.getReceiverAddress(),
                items,
                formatMoney(order.getSubtotal()),
                formatMoney(order.getShippingFee()),
                formatMoney(order.getDiscountAmount()),
                formatMoney(order.getTotalAmount()),
                order.getPaymentMethod(),
                order.getPaymentStatus()
        );
    }

    private static String formatItem(OrderItem item) {
        return "- %s x%d: %s".formatted(
                item.getProductName(),
                item.getQuantity(),
                formatMoney(item.getTotalPrice())
        );
    }

    private static String formatMoney(BigDecimal value) {
        BigDecimal amount = value == null ? BigDecimal.ZERO : value;
        return NumberFormat.getCurrencyInstance(VIETNAM).format(amount);
    }
}
