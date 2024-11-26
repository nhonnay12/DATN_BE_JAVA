package com.datn.mapper;

import com.datn.dto.response.ProductResponse;
import com.datn.dto.request.product_cate_cart.ProductRequest;
import com.datn.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface ProductMapper {
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "publisher", ignore = true)
    @Mapping(target = "user", ignore = true)
    Product toProduct(ProductRequest request);
    @Mapping(target = "category", source = "category")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "linkDrive", source = "linkDrive")
    ProductResponse toProductResponse(Product product);
}
