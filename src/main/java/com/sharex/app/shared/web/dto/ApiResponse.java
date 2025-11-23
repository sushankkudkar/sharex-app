package com.sharex.app.shared.web.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
@JsonPropertyOrder({ "success", "data", "error" })
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private ApiError error;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.of(true, data, null);
    }

    public static <T> ApiResponse<T> error(ApiError err) {
        return ApiResponse.of(false, null, err);
    }
}
