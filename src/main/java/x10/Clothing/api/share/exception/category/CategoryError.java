package x10.Clothing.api.share.exception.category;

import x10.Clothing.api.share.exception.ErrorDescriptor;

public enum CategoryError implements ErrorDescriptor {
    CATEGORY_EXISTS(
            "BUSINESS",
            409,
            "CATEGORY.EXISTS",
            "Danh mục đã tồn tại"
    ),
    CATEGORY_NOT_FOUND(
            "BUSINESS",
            404,
            "CATEGORY.NOT_FOUND",
            "Không tìm thấy danh mục"
    );

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    CategoryError(String type, int httpStatus, String code, String defaultMessage) {
        this.type = type;
        this.httpStatus = httpStatus;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public int httpStatus() { return httpStatus; }

    @Override
    public String code() { return code; }

    @Override
    public String defaultMessage() { return defaultMessage; }

    @Override
    public String type() { return type; }
}

