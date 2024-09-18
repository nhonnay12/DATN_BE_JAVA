package com.datn.service.impl;

import com.datn.models.dto.request.product_cate_cart.CategoryRequest;
import com.datn.models.dto.request.product_cate_cart.CategoryUpdate;
import com.datn.models.dto.response.CategoryResponse;
import com.datn.models.entity.Category;
import com.datn.models.exception.AppException;
import com.datn.models.exception.ErrorCode;
import com.datn.models.mapper.CategoryMapper;
import com.datn.repository.CategoryRepo;
import com.datn.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = categoryMapper.toCategory(request);
        categoryRepo.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public List<CategoryResponse> getAllCategory() {

        return categoryRepo.findAll().stream().map(categoryMapper::toCategoryResponse).toList();
    }

    @Override
    public CategoryResponse updateCategory(CategoryUpdate request) {
        var category = categoryRepo.findById(request.getId()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        category.setId(request.getId());
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        categoryRepo.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }
}
