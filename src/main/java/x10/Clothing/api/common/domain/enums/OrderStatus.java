package x10.Clothing.api.common.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PROCESSING,
    SHIPPING,
    DELIVERED,
    CANCELLED,
    RETURNED;

    @JsonCreator
    public static OrderStatus from(String value) {
        if (value == null || value.isBlank()) {
            return PENDING;
        }

        return OrderStatus.valueOf(value.trim().toUpperCase());
    }
}
