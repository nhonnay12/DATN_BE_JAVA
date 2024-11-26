package com.datn.mapper;

import com.datn.dto.request.product_cate_cart.CategoryRequest;
import com.datn.dto.response.CategoryResponse;
import com.datn.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequest categoryRequest);
    CategoryResponse toCategoryResponse(Category category);
}
