package com.datn.service;



import com.datn.models.dto.request.product_cate_cart.CategoryRequest;
import com.datn.models.dto.request.product_cate_cart.CategoryUpdate;
import com.datn.models.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    List<CategoryResponse> getAllCategory();
    CategoryResponse updateCategory(CategoryUpdate request);
    void deleteCategory(Long id);
}
