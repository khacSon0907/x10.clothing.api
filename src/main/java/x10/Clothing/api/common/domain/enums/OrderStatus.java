package x10.Clothing.api.common.domain.enums;

public enum OrderStatus {

    PENDING,      // Chờ xác nhận

    CONFIRMED,    // Đã xác nhận

    PROCESSING,   // Đang chuẩn bị hàng

    SHIPPING,     // Đang giao hàng

    DELIVERED,    // Đã giao thành công

    CANCELLED,    // Đã hủy

    RETURNED      // Trả hàng
}