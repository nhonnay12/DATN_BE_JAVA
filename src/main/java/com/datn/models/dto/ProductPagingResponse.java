package com.datn.models.dto;

import com.datn.models.dto.response.ProductResponse;

import java.util.List;

public record ProductPagingResponse(List<ProductResponse> products,
                                    Integer pageNumber,
                                    Integer pageSize,
                                    Long totalElements,
                                    int totalPages,
                                    boolean isLast
    ) {
}
