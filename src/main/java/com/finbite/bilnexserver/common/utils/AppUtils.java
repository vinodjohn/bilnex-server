package com.finbite.bilnexserver.common.utils;

import com.finbite.bilnexserver.common.models.ErrorResponse;
import org.springframework.data.domain.Sort;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A helper class to provide common functionalities for this app
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
public class AppUtils {
    public static Sort getSortOfColumn(String sort, String order) {
        return order.equalsIgnoreCase("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending();
    }

    public static ErrorResponse getErrorResponse(String description, String entity) {
        List<String> details = new ArrayList<>();
        details.add(description);

        return new ErrorResponse(MessageFormat.format("{0} not found!", entity), details);
    }

    public static String getExceptionMessage(String className, String fieldName, boolean isAlreadyExists) {
        return isAlreadyExists ? MessageFormat.format("{0} {1} already exists!", className, fieldName) :
                MessageFormat.format("Invalid {0} {1}", className, fieldName);
    }
}