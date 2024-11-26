package com.datn.controller;

import com.datn.dto.PageResponse;
import com.datn.dto.RequestDTO;
import com.datn.dto.response.ProductResponse;
import com.datn.entity.Product;
import com.datn.mapper.ProductMapper;
import com.datn.repository.ProductRepository;
import com.datn.service.impl.FilterSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
public class FilterController {

    @Autowired
    FilterSpecification filterSpecification;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductMapper productMapper;
    // join
    // tim theo 1 product , ten tac gia
    @PostMapping("/filter/page/{page}")
    public ResponseEntity<PageResponse<ProductResponse>> filterWithPagination(
            @RequestBody RequestDTO requestDTO,
            @PathVariable int page,
            @RequestParam(defaultValue = "12") int size) {

        // Tạo Specification từ requestDTO
        Specification<Product> specification = filterSpecification.getSearchSpecification(
                requestDTO.getSearchRequestDTO(),
                requestDTO.getGlobalOperator()
        );
        // Tạo PageRequest với sắp xếp theo cân nặng (weight)
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name").ascending());

        // Lấy kết quả từ repository
        Page<Product> result = productRepository.findAll(specification, pageRequest);

        // Chuyển đổi kết quả thành DTO
        PageResponse<ProductResponse> response = new PageResponse<>(
                result.getContent().stream()
                        .map(productMapper::toProductResponse)
                        .collect(Collectors.toList()),
                result.getTotalPages()
        );

        return ResponseEntity.ok(response);
    }
}
