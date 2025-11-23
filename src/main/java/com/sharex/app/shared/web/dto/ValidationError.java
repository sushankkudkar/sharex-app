package com.sharex.app.shared.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class ValidationError {
    private String field;
    private String message;
    private Object rejectedValue;
}
