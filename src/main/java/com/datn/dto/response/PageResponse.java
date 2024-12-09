package com.datn.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse <T>{
    private int pageNumber; // currentPage
    private int pageSize;
    private int totalPages;
    private int totalElements;
    @Builder.Default
    private List<T> content= Collections.emptyList();
}
