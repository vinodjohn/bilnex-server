package com.finbite.bilnexserver.common.utils;

import com.finbite.bilnexserver.common.models.ErrorResponse;
import org.springframework.data.domain.Sort;
import org.springframework.util.ClassUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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

    public static String getStringOfClassName(Class<?> clazz) {
        String className = ClassUtils.getShortName(clazz.getSimpleName());
        className = Pattern.compile("(?<=[a-z])(?=[A-Z])")
                .matcher(className)
                .replaceAll(" ");

        return className;
    }

    public static ErrorResponse getErrorResponse(String description, Class<?> clazz) {
        String className = getStringOfClassName(clazz);
        List<String> details = new ArrayList<>();
        details.add(description);

        return new ErrorResponse(MessageFormat.format("{0} not found!", className), details);
    }
}