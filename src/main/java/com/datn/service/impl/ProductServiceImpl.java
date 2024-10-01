package com.datn.service.impl;

import com.datn.controller.ImageController;
import com.datn.models.dto.ProductPagingResponse;
import com.datn.models.dto.request.product_cate_cart.ProductRequest;
import com.datn.models.dto.request.product_cate_cart.ProductUpdate;
import com.datn.models.dto.response.ProductResponse;
import com.datn.models.entity.Author;
import com.datn.models.entity.Category;
import com.datn.models.entity.ImageData;
import com.datn.models.entity.Product;
import com.datn.models.exception.AppException;
import com.datn.models.exception.ErrorCode;
import com.datn.models.mapper.ProductMapper;
import com.datn.repository.AuthorRepo;
import com.datn.repository.CategoryRepo;
import com.datn.repository.ProductRepository;
import com.datn.repository.PublisherRepo;
import com.datn.service.ImageService;
import com.datn.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.apache.el.lang.ELArithmetic.isNumber;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private AuthorRepo authorRepo;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageController imageController;
    @Autowired
    private PublisherRepo publisherRepo;

    @Override
    public ProductResponse getProduct(Long id) {
        log.info("Get product in DB ");
        var product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        return productMapper.toProductResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        log.info("getAllProducts in DB");
        return productRepository.findAll().stream().map(productMapper::toProductResponse).toList();
    }

    @Override
    public ProductResponse addProduct(MultipartFile files, ProductRequest productRequest) throws IOException {
        if (productRequest.getPrice() == null || !(isNumber(productRequest.getPrice()))) {
            throw new AppException(ErrorCode.PRICE_INVALID);
        }
        // Chuyển đổi từ ProductRequest sang Product
        Product product = productMapper.toProduct(productRequest);

        ImageData image = imageService.saveImage(files);
        image.setProduct(product);
        // Thiết lập hình ảnh cho sản phẩm
        product.setImages(Collections.singletonList(image));

        Category category = categoryRepo.findById(productRequest.getCategory_id()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        product.setCategory(category);

        Author author = authorRepo.findById(productRequest.getAuthor_id()).orElse(null);
        product.setAuthors(new HashSet<>(Collections.singletonList(author)));

        // publisher
        var publisher = publisherRepo.findById(productRequest.getPublisher_id()).orElseThrow(() -> new AppException(ErrorCode.PUBLISHER_NOT_EXISTED));
        product.setPublisher(publisher);
        //log.info("author" + author.toString());
        productRepository.save(product);

        return productMapper.toProductResponse(product);
    }


    @Override
    public ProductResponse updateProduct(ProductUpdate productUpdate) {

        var product = productRepository.findById(productUpdate.getId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        product.setName(productUpdate.getName());
        product.setDescription(productUpdate.getDescription());
        product.setPrice(productUpdate.getPrice());
        productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductPagingResponse getAllProductWithPagingAndSort(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
        Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort); // vi pgaeNumber bat dau tu 1 nen khi lay page se lay pageNumber - 1 vì khi lấy nó lay từ 0
        Page<Product> productPage = productRepository.findAll(pageable);
        List<Product> productList = productPage.getContent();

        List<ProductResponse> productResponseList = new ArrayList<>();
        for (Product product : productList) {
            ProductResponse productResponse = productMapper.toProductResponse(product);
            productResponseList.add(productResponse);
        }

        return new ProductPagingResponse(productResponseList,
                pageNumber, pageSize, productPage.getTotalElements(),
                productPage.getTotalPages(), productPage.isLast());
    }

    @Override
    public ProductPagingResponse getAllProductwithPaging(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);// vi pgaeNumber bat dau tu 1 nen khi lay page se lay pageNumber - 1 vì khi lấy nó lay từ 0
        Page<Product> productPage = productRepository.findAll(pageable);
        List<Product> productList = productPage.getContent();

        List<ProductResponse> productResponseList = new ArrayList<>();
        for (Product product : productList) {
            ProductResponse productResponse = productMapper.toProductResponse(product);
            productResponseList.add(productResponse);
        }

        return new ProductPagingResponse(productResponseList,
                pageNumber, pageSize, productPage.getTotalElements(),
                productPage.getTotalPages(), productPage.isLast());
    }
}
