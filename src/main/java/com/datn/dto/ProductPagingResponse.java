package com.datn.dto;

import com.datn.dto.response.ProductResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.List;

//public record ProductPagingResponse(List<ProductResponse> products,
//                                    Integer pageNumber,
//                                    Integer pageSize,
//                                    Long totalElements,
//                                    int totalPages,
//                                    boolean isLast
//    ) {
//}
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductPagingResponse <T>{
     int pageNumber; // currentPage
     int pageSize;
     int totalPages;
     int totalElements;
    boolean isLast;
    @Builder.Default
     List<T> products= Collections.emptyList();
}
