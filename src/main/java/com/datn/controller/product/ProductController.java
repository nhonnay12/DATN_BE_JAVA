package com.datn.controller.product;

import com.datn.constant.PageParam;
import com.datn.models.dto.ProductPagingResponse;
import com.datn.models.dto.response.ProductResponse;
import com.datn.models.dto.request.product_cate_cart.ProductRequest;
import com.datn.models.dto.request.product_cate_cart.ProductUpdate;
import com.datn.models.dto.response.ApiResponse;
import com.datn.service.ProductService;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @CacheEvict(cacheNames = "cache1", allEntries = true) // Xóa tất cả các entry trong cache1
    @PostMapping
    public ApiResponse<ProductResponse> addProduct(@RequestParam("file") MultipartFile files,
                                                   @RequestParam("name") String name,
                                                   @RequestParam("description") String description,
                                                   @RequestParam("price") Double price,
                                                   @RequestParam("quantity") Integer quantity,
                                                   @RequestParam("status") boolean status,
                                                   @RequestParam("category_id") Long category_id,
                                                   @RequestParam("author_id") Integer author_id,
                                                   @RequestParam("publisher_id") Integer publisher_id
                                                   ) throws IOException {

        ProductRequest productRequest = ProductRequest.builder()
                .name(name)
                .description(description)
                .price(price)
                .quantity(quantity)
                .status(status)
                .category_id(category_id)
                .author_id(author_id)
                .publisher_id(publisher_id)
                .build();

        ApiResponse<ProductResponse> apiResponse = ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("Success")
                .result(productService.addProduct(files, productRequest))
                .build();

        return apiResponse;
    }


    @Cacheable(cacheNames = "cache1", key = "'product'")
    @GetMapping(value = "/getall")
    public ApiResponse<List<ProductResponse>> getAllProducts() {
        ApiResponse<List<ProductResponse>> apiResponse = ApiResponse.<List<ProductResponse>>builder()
                .code(200)
                .message("Success")
                .result(productService.getAllProducts())
                .build();
        return apiResponse;
    }

    @Cacheable(cacheNames = "cache2", key = "'product_' + #id")
    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable Long id) {
        ApiResponse<ProductResponse> apiResponse = ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("Success")
                .result(productService.getProduct(id))
                .build();
        return apiResponse;
    }

    @CachePut(cacheNames = "cache2", key = "'product_' + #productUpdate.getId()")
    @CacheEvict(cacheNames = "cache1", allEntries = true) // Xóa tất cả các entry trong cache1
    @PutMapping("/update")
    public ApiResponse<ProductResponse> updateProduct(@RequestBody ProductUpdate productUpdate) {
        ApiResponse<ProductResponse> apiResponse = ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("Success")
                .result(productService.updateProduct(productUpdate))
                .build();
        return apiResponse;
    }
    @CacheEvict(cacheNames = "cache1", allEntries = true) // Xóa tất cả các entry trong cache1
    @DeleteMapping("delete/{id}")
    public ApiResponse<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProduct(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(200)
                .message("Delete sucessful")
                .build();
        return apiResponse;
    }
    @GetMapping("/getProductPaging")
    public ApiResponse<ProductPagingResponse> getAllProductWithPaging(
            @RequestParam(defaultValue = PageParam.PAGE_NUM) Integer pageNumber,
            @RequestParam(defaultValue = PageParam.PAGE_SIZE) Integer pageSize
    ){
        return ApiResponse.<ProductPagingResponse>builder()
                .result(productService.getAllProductwithPaging(pageNumber, pageSize))
                .build();

    }
    @GetMapping("/getProductPagingSort")
    public ApiResponse<ProductPagingResponse> getAllProductWithPagingSort(
            @RequestParam(defaultValue = PageParam.PAGE_NUM) Integer pageNumber,
            @RequestParam(defaultValue = PageParam.PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = PageParam.SORT_BY) String sortBy,
            @RequestParam(defaultValue = PageParam.SORT_DIR) String sortDir
    ){
        return ApiResponse.<ProductPagingResponse>builder()
                .result(productService.getAllProductWithPagingAndSort(pageNumber, pageSize, sortBy, sortDir))
                .build();

    }
}
