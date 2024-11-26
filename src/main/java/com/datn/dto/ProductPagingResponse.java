package com.datn.dto;

import com.datn.dto.response.ProductResponse;

import java.util.List;

public record ProductPagingResponse(List<ProductResponse> products,
                                    Integer pageNumber,
                                    Integer pageSize,
                                    Long totalElements,
                                    int totalPages,
                                    boolean isLast
    ) {
}
