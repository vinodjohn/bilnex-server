package com.finbite.bilnexserver.auth.dtos;

import java.util.List;

/**
 * List DTO
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
public record ObjectListDto(List<?> objList, int currentPage, long totalElements) {
}

