package x10.Clothing.api.share.response;

import lombok.experimental.SuperBuilder;
import x10.Clothing.api.share.BaseResponse;

import java.time.Instant;

@SuperBuilder
public final class ApiResponse<T> extends BaseResponse {

    public static <T> ApiResponse<T> success(
            int status,
            String code,
            String message,
            T data,
            String path,
            String traceId
    ) {
        return ApiResponse.<T>builder()
                .type("SUCCESS")
                .title("Success")
                .status(status)
                .code(code == null ? "SUCCESS" : code)
                .message(message)
                .data(data)
                .path(path)
                .traceId(traceId)
                .timestamp(Instant.now())
                .build();
    }
}
