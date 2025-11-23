package com.sharex.app.shared.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
public class ApiError {
    private String message;
    private String code;
    private List<ValidationError> errors;
    private Instant timestamp;
}
