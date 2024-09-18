package com.datn.models.mapper;

import com.datn.models.dto.response.ProductResponse;
import com.datn.models.dto.request.product_cate_cart.ProductRequest;
import com.datn.models.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface ProductMapper {
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "publisher", ignore = true)
    Product toProduct(ProductRequest request);
    @Mapping(target = "category", source = "category")
    ProductResponse toProductResponse(Product product);
}
