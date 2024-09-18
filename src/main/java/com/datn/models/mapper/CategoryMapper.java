package com.datn.models.mapper;

import com.datn.models.dto.request.product_cate_cart.CategoryRequest;
import com.datn.models.dto.response.CategoryResponse;
import com.datn.models.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequest categoryRequest);
    CategoryResponse toCategoryResponse(Category category);
}
