package com.datn.mapper;


import com.datn.dto.response.ProductResponse;
import com.datn.dto.request.product_cate_cart.ProductRequest;
import com.datn.entity.Product;
import com.datn.entity.ImageData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMapper {

    // Ánh xạ từ ProductRequest sang Product
    public Product toProduct(ProductRequest request) {
        if (request == null) {
            return null;
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setStatus(request.isStatus());
        product.setLinkDrive(request.getLinkDrive());

        // Các trường bị bỏ qua: category, author, publisher, user, images
        return product;
    }

    // Ánh xạ từ Product sang ProductResponse
    public ProductResponse toProductResponse(Product product) {
        if (product == null) {
            return null;
        }

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setQuantity(product.getQuantity());
        response.setStatus(product.isStatus());
        response.setLinkDrive(product.getLinkDrive());
        response.setCategory(product.getCategory());
        response.setAuthor(product.getAuthor());
        response.setPublisher(product.getPublisher());
        response.setUser(product.getUser());

        // Sao chép danh sách ảnh
        List<ImageData> images = product.getImages();
        if (images != null) {
            response.setImages(new ArrayList<>(images));
        }

        return response;
    }
}

