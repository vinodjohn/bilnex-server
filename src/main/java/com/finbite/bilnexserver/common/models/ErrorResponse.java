package com.finbite.bilnexserver.common.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Model for Error response
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private List<String> details;
}
