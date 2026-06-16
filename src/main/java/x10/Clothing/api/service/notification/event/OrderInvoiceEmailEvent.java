package x10.Clothing.api.service.notification.event;

import x10.Clothing.api.common.domain.entities.OrderEntity;

public record OrderInvoiceEmailEvent(
        String email,
        String username,
        OrderEntity order
) {
}
