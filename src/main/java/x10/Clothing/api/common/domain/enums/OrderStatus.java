package x10.Clothing.api.common.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OrderStatus {
    PENDING,

    CONFIRMED,

    SHIPPING,

    DELIVERED,

    COMPLETED,

    RETURN_REQUESTED,

    RETURN_APPROVED,

    RETURN_RECEIVED,

    EXCHANGE_APPROVED,

    EXCHANGED,

    REFUND_PROCESSING,

    REFUNDED,

    RETURNED,

    CANCELLED;

    @JsonCreator
    public static OrderStatus from(String value) {
        if (value == null || value.isBlank()) {
            return PENDING;
        }

        String normalized = value.trim().toUpperCase();
        if ("CANCELED".equals(normalized)) {
            return CANCELLED;
        }

        return OrderStatus.valueOf(normalized);
    }
}
