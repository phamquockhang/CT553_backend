package com.pqkhang.ct553_backend.domain.booking.order.enums;

public enum PaymentStatusEnum {
    COD,              // Thanh toán khi nhận hàng
    PENDING,          // Đang chờ thanh toán
    FAILED,           // Thanh toán thất bại
    CANCELLED,        // Giao dịch bị hủy
    SUCCESS,          // Thanh toán thành công
        EXPIRED,          // Giao dịch hết hạn
    ERROR             // Lỗi hệ thống hoặc không xác định
}
