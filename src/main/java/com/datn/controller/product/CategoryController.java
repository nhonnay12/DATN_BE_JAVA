package com.datn.controller.product;



import com.datn.models.dto.request.product_cate_cart.CategoryUpdate;
import com.datn.models.dto.response.ApiResponse;
import com.datn.models.dto.response.CategoryResponse;
import com.datn.service.CategoryService;
import com.datn.models.dto.request.product_cate_cart.CategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) {
        ApiResponse<CategoryResponse> apiResponse = ApiResponse.<CategoryResponse>builder()
                .code(200)
                .message("Sucess")
                .result(categoryService.createCategory(categoryRequest))
                .build();
        return apiResponse;
    }
    @PutMapping
    public ApiResponse<CategoryResponse> updateCategory(@RequestBody CategoryUpdate categoryUpdate) {
        ApiResponse<CategoryResponse> apiResponse = ApiResponse.<CategoryResponse>builder()
                .code(200)
                .message("Sucess")
                .result(categoryService.updateCategory(categoryUpdate))
                .build();
        return apiResponse;
    }
    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategorys() {
        ApiResponse<List<CategoryResponse>> apiResponse = ApiResponse.<List<CategoryResponse>>builder()
                .code(200)
                .message("Sucess")
                .result(categoryService.getAllCategory())
                .build();
        return apiResponse;
    }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(200)
                .message("Category " + id + " is deleted")
                .build();
        return apiResponse;
    }

}
