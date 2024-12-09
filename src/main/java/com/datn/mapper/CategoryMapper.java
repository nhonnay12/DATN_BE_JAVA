package com.datn.mapper;

import com.datn.dto.request.product_cate_cart.CategoryRequest;
import com.datn.dto.response.CategoryResponse;
import com.datn.entity.Category;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Component
public class CategoryMapper {


    public Category toCategory(CategoryRequest categoryRequest) {
        if ( categoryRequest == null ) {
            return null;
        }

        Category category = new Category();

        category.setName( categoryRequest.getName() );
        category.setDescription( categoryRequest.getDescription() );
        category.setStatus( categoryRequest.getStatus() );

        return category;
    }


    public CategoryResponse toCategoryResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryResponse.CategoryResponseBuilder categoryResponse = CategoryResponse.builder();

        categoryResponse.id( category.getId() );
        categoryResponse.name( category.getName() );
        categoryResponse.description( category.getDescription() );
        categoryResponse.status( category.getStatus() );

        return categoryResponse.build();
    }
}
