package com.datn.service;

import com.datn.dto.ProductPagingResponse;
import com.datn.dto.request.product_cate_cart.ProductUpdate;
import com.datn.dto.response.PageResponse;
import com.datn.dto.response.ProductResponse;
import com.datn.dto.request.product_cate_cart.ProductRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    ProductPagingResponse getAllProductwithPagingWithAuthor(Integer pageNumber, Integer pageSize, Long author_id);

    ProductResponse getProduct(Long id);
    List<ProductResponse> getAllProducts();
    ProductResponse addProduct(MultipartFile files, ProductRequest productRequest) throws IOException;
    ProductResponse updateProduct(ProductUpdate productUpdate, MultipartFile file);
    void deleteProduct(Long id);
    ProductPagingResponse getAllProductWithPagingAndSort(Integer pageNumber, Integer pageSize, String sortBy, String dir);
    ProductPagingResponse getAllProductwithPaging(Integer pageNumber, Integer pageSize);
    ProductPagingResponse getAllProductwithPagingWithCategory(Integer pageNumber, Integer pageSize, Long category_id);
    ProductPagingResponse getAllProductwithPagingWithPublisher(Integer pageNumber, Integer pageSize, Long publisher_id);
    ProductPagingResponse getAllProductwithPagingWithUser(Integer pageNumber, Integer pageSize);
    PageResponse getABCProduct (long authId, int page, int size);
}
