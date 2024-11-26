package com.datn.service.impl;

import com.datn.dto.request.product_cate_cart.CategoryRequest;
import com.datn.dto.request.product_cate_cart.CategoryUpdate;
import com.datn.dto.response.CategoryResponse;
import com.datn.entity.Category;
import com.datn.exception.AppException;
import com.datn.exception.ErrorCode;
import com.datn.mapper.CategoryMapper;
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
        if(categoryRepo.findByName(request.getName()).isPresent()){
            throw new AppException(ErrorCode.Category_EXISTED);
        }
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
        category.setStatus(request.getStatus());
        categoryRepo.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepo.findById(id).orElse(null);
        category.setStatus("INACTIVE");
        categoryRepo.save(category);
    }
}
