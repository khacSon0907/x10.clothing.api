package x10.Clothing.api.common.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PaymentStatus {
    UNPAID,
    PAID,
    REFUNDED,
    FAILED;

    @JsonCreator
    public static PaymentStatus from(String value) {
        if (value == null || value.isBlank()) {
            return UNPAID;
        }

        return PaymentStatus.valueOf(value.trim().toUpperCase());
    }
}
