package x10.Clothing.api.share.exception.user;


import x10.Clothing.api.share.exception.ErrorDescriptor;

public enum UserError implements ErrorDescriptor {

    EMAIL_EXISTS(
            "BUSINESS",
            409,
            "USER.EMAIL_EXISTS",
            "Email đã tồn tại"
    ),
    USER_NOT_FOUND(
            "BUSINESS",
            404,
            "USER.NOT_FOUND",
            "Không tìm thấy người dùng"
    ),
    INVALID_OTP(
            "BUSINESS",
            400,
            "AUTH.INVALID_OTP",
            "OTP không hợp lệ hoặc đã hết hạn"
    ),
    INVALID_CREDENTIALS(
            "BUSINESS",
            400,
            "AUTH.INVALID_CREDENTIALS",
            "Tài khoản hoặc mật khẩu không chính xác"
    ),
    USER_NOT_ACTIVE(
            "BUSINESS",
            403,
            "AUTH.USER_NOT_ACTIVE",
            "Tài khoản chưa được kích hoạt hoặc đã bị khóa"
    ),
    EMAIL_NOT_VERIFIED(
            "BUSINESS",
            403,
            "AUTH.EMAIL_NOT_VERIFIED",
            "Email chưa được xác thực"
    ),
    LOGIN_LOCKED(
            "BUSINESS",
            429,
            "AUTH.LOGIN_LOCKED",
            "Tài khoản đã bị khóa tạm thời do nhập sai mật khẩu quá 5 lần. Vui lòng thử lại sau 5 phút."
    );


    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    UserError(String type, int httpStatus, String code, String defaultMessage) {
        this.type = type;
        this.httpStatus = httpStatus;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public int httpStatus() {
        return httpStatus;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String defaultMessage() {
        return defaultMessage;
    }
}