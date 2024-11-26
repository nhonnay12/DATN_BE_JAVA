package com.datn.service.impl;

import com.datn.controller.ImageController;
import com.datn.dto.ProductPagingResponse;
import com.datn.dto.request.product_cate_cart.ProductRequest;
import com.datn.dto.request.product_cate_cart.ProductUpdate;
import com.datn.dto.response.ProductResponse;
import com.datn.entity.*;

import com.datn.exception.AppException;
import com.datn.exception.ErrorCode;
import com.datn.mapper.ProductMapper;
import com.datn.repository.*;
import com.datn.service.ImageService;
import com.datn.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageRepo imageRepo;
@Autowired
private CategoryRepo categoryRepository;
@Autowired
private  PublisherRepo publisherRepository;
@Autowired
private AuthorRepo authorRepository;
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

        if(productRepository.findByName(productRequest.getName()).isPresent()){
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
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
        product.setAuthor(author);

        // publisher
        var publisher = publisherRepo.findById(productRequest.getPublisher_id()).orElseThrow(() -> new AppException(ErrorCode.PUBLISHER_NOT_EXISTED));
        product.setPublisher(publisher);

        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        product.setUser(user);

        //log.info("author" + author.toString());
        productRepository.save(product);


        return productMapper.toProductResponse(product);
    }


    @Override
    public ProductResponse updateProduct(ProductUpdate productUpdate, MultipartFile file) {
        var product = productRepository.findById(productUpdate.getId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
if(product.equals(productUpdate)) {
    throw new AppException(ErrorCode.PRODUCT_NOT_CHANGES);
}
        // Kiểm tra và cập nhật tên sản phẩm nếu có thay đổi
        if (productUpdate.getName() != null && !productUpdate.getName().equals(product.getName())) {
            product.setName(productUpdate.getName());
        }

        // Kiểm tra và cập nhật mô tả sản phẩm nếu có thay đổi
        if (productUpdate.getDescription() != null && !productUpdate.getDescription().equals(product.getDescription())) {
            product.setDescription(productUpdate.getDescription());
        }

        // Kiểm tra và cập nhật giá sản phẩm nếu có thay đổi
        if (productUpdate.getPrice() != null && !productUpdate.getPrice().equals(product.getPrice())) {
            product.setPrice(productUpdate.getPrice());
        }

        // Kiểm tra và cập nhật trạng thái sản phẩm nếu có thay đổi
        if (productUpdate.isStatus() != product.isStatus()) {
            product.setStatus(productUpdate.isStatus());
        }

        // Kiểm tra và cập nhật link drive nếu có thay đổi
        if (productUpdate.getLinkDrive() != null && !productUpdate.getLinkDrive().equals(product.getLinkDrive())) {
            product.setLinkDrive(productUpdate.getLinkDrive());
        }
        // Kiểm tra và cập nhật tác giả (author) nếu có thay đổi
        if (productUpdate.getAuthor_id() != null) {
            if (product.getAuthor() == null || !product.getAuthor().getId().equals(productUpdate.getAuthor_id())) {
                // Cập nhật tác giả
                var author = authorRepository.findById(productUpdate.getAuthor_id())
                        .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_EXISTED));
                product.setAuthor(author);
            }
        }

        // Kiểm tra và cập nhật thể loại (category) nếu có thay đổi
        if (productUpdate.getCategory_id() != null && !product.getCategory().getId().equals(productUpdate.getCategory_id())) {
            // Cập nhật thể loại
            var category = categoryRepository.findById(productUpdate.getCategory_id())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
            product.setCategory(category);
        }

        // Kiểm tra và cập nhật nhà xuất bản (publisher) nếu có thay đổi
        if (productUpdate.getPublisher_id() != null) {
            if (product.getPublisher() == null || !product.getPublisher().getId().equals(productUpdate.getPublisher_id())) {
                // Cập nhật nhà xuất bản
                var publisher = publisherRepository.findById(productUpdate.getPublisher_id())
                        .orElseThrow(() -> new AppException(ErrorCode.PUBLISHER_NOT_EXISTED));
                product.setPublisher(publisher);
            }
        }

        // Kiểm tra và xử lý hình ảnh mới
        if (file != null && !file.isEmpty()) {
            // Nếu có ảnh mới, ẩn tất cả ảnh cũ
            for (ImageData img : product.getImages()) {
                img.setHidden(true);
                imageRepo.save(img);  // Lưu lại trạng thái ẩn cho ảnh cũ
            }

            // Lưu ảnh mới
            ImageData newImage = imageService.saveImage(file);
            newImage.setProduct(product);
            newImage.setHidden(false);  // Đảm bảo ảnh mới không bị ẩn
            List<ImageData> images = new ArrayList<>(product.getImages());
            images.add(newImage);  // Thêm ảnh mới vào danh sách ảnh của người dùng
            product.setImages(images);
        }
        // Lưu lại sản phẩm đã được cập nhật


        // Chuyển đổi đối tượng sản phẩm sang ProductResponse và trả về
        return productMapper.toProductResponse( productRepository.save(product));
    }


    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        product.setStatus(false);
        productRepository.save(product);
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
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);// vi pgaeNumber bat dau tu 1 nen khi lay page se lay pageNumber - 1 vì khi lấy nó lay từ 0
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
    public ProductPagingResponse getAllProductwithPagingWithCategory(Integer pageNumber, Integer pageSize, Long category_id) {
        // Tạo Pageable cho phân trang
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize); // vì pageNumber bắt đầu từ 1, nên giảm đi 1
        // Lọc sản phẩm theo category_id
        Page<Product> productPage = productRepository.findByCategoryId(category_id, pageable);
        List<Product> productList = productPage.getContent();

        // Chuyển đổi thành ProductResponse
        List<ProductResponse> productResponseList = new ArrayList<>();
        for (Product product : productList) {
            if (product.isStatus()) {
                ProductResponse productResponse = productMapper.toProductResponse(product);
                productResponseList.add(productResponse);
            }
        }
        // Trả về đối tượng ProductPagingResponse
        return new ProductPagingResponse(productResponseList,
                pageNumber, pageSize, productPage.getTotalElements(),
                productPage.getTotalPages(), productPage.isLast());
    }

    @Override
    public ProductPagingResponse getAllProductwithPagingWithPublisher(Integer pageNumber, Integer pageSize, Long pulisher_id) {
        // Tạo Pageable cho phân trang
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize); // vì pageNumber bắt đầu từ 1, nên giảm đi 1
        // Lọc sản phẩm theo category_id
        Page<Product> productPage = productRepository.findByPublisherId(pulisher_id, pageable);
        List<Product> productList = productPage.getContent();

        // Chuyển đổi thành ProductResponse
        List<ProductResponse> productResponseList = new ArrayList<>();
        for (Product product : productList) {
            ProductResponse productResponse = productMapper.toProductResponse(product);
            productResponseList.add(productResponse);
        }

        // Trả về đối tượng ProductPagingResponse
        return new ProductPagingResponse(productResponseList,
                pageNumber, pageSize, productPage.getTotalElements(),
                productPage.getTotalPages(), productPage.isLast());
    }

    @Override
    public ProductPagingResponse getAllProductwithPagingWithAuthor(Integer pageNumber, Integer pageSize, Long author_id) {
        // Tạo Pageable cho phân trang
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize); // vì pageNumber bắt đầu từ 1, nên giảm đi 1
        // Lọc sản phẩm theo category_id
        Page<Product> productPage = productRepository.findByAuthorId(author_id, pageable);
        List<Product> productList = productPage.getContent();

        // Chuyển đổi thành ProductResponse
        List<ProductResponse> productResponseList = new ArrayList<>();
        for (Product product : productList) {
            ProductResponse productResponse = productMapper.toProductResponse(product);
            productResponseList.add(productResponse);
        }

        // Trả về đối tượng ProductPagingResponse
        return new ProductPagingResponse(productResponseList,
                pageNumber, pageSize, productPage.getTotalElements(),
                productPage.getTotalPages(), productPage.isLast());
    }
    @Override
    public ProductPagingResponse getAllProductwithPagingWithUser(Integer pageNumber, Integer pageSize) {
        // Tạo Pageable cho phân trang
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize); // vì pageNumber bắt đầu từ 1, nên giảm đi 1
        // Lọc sản phẩm theo category_id
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Page<Product> productPage = productRepository.findByUserId(user.getId(), pageable);
        List<Product> productList = productPage.getContent();

        // Chuyển đổi thành ProductResponse
        List<ProductResponse> productResponseList = new ArrayList<>();
        for (Product product : productList) {
            ProductResponse productResponse = productMapper.toProductResponse(product);
            productResponseList.add(productResponse);
        }

        // Trả về đối tượng ProductPagingResponse
        return new ProductPagingResponse(productResponseList,
                pageNumber, pageSize, productPage.getTotalElements(),
                productPage.getTotalPages(), productPage.isLast());
    }
}
