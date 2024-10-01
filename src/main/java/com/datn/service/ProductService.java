package com.datn.service;

import com.datn.models.dto.ProductPagingResponse;
import com.datn.models.dto.request.product_cate_cart.ProductUpdate;
import com.datn.models.dto.response.ProductResponse;
import com.datn.models.dto.request.product_cate_cart.ProductRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    ProductResponse getProduct(Long id);
    List<ProductResponse> getAllProducts();
    ProductResponse addProduct(MultipartFile files, ProductRequest productRequest) throws IOException;
    ProductResponse updateProduct(ProductUpdate productUpdate);
    void deleteProduct(Long id);
    public ProductPagingResponse getAllProductWithPagingAndSort(Integer pageNumber, Integer pageSize, String sortBy, String dir);
    public ProductPagingResponse getAllProductwithPaging(Integer pageNumber, Integer pageSize);
}
