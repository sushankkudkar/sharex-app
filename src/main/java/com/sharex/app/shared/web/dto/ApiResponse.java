package com.sharex.app.shared.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.slf4j.MDC;

import static com.sharex.app.shared.web.filter.CorrelationId.MDC_KEY;

@Getter
@JsonPropertyOrder({ "success", "requestId", "data", "error" })
public final class ApiResponse<T> {

    private final boolean success;
    private final String requestId;
    private final T data;
    private final ApiError error;

    private ApiResponse(boolean success, String requestId, T data, ApiError error) {
        this.success = success;
        this.requestId = requestId;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, MDC.get(MDC_KEY), data, null);
    }

    public static <T> ApiResponse<T> failure(ApiError error) {
        return new ApiResponse<>(false, MDC.get(MDC_KEY), null, error);
    }

}
