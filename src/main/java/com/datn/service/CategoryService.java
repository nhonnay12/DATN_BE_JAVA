package com.datn.service;



import com.datn.dto.request.product_cate_cart.CategoryRequest;
import com.datn.dto.request.product_cate_cart.CategoryUpdate;
import com.datn.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    List<CategoryResponse> getAllCategory();
    CategoryResponse updateCategory(CategoryUpdate request);
    void deleteCategory(Long id);
}
