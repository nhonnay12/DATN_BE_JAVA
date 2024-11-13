package com.datn.models.dto.response;

import java.util.List;

public record UserPagingResponse(List<UserResponse> users,
                                 Integer pageNumber,
                                 Integer pageSize,
                                 Long totalElements,
                                 int totalPages,
                                 boolean isLast
    ) {
}
