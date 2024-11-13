package com.datn.controller.product;


import com.datn.models.dto.request.author_publisher.PublisherRequest;
import com.datn.models.dto.request.author_publisher.PublisherUpdate;
import com.datn.models.dto.response.ApiResponse;

import com.datn.models.dto.response.author_publisher.PublisherResponse;
import com.datn.service.PublisherService;
import com.datn.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publisher")
public class PublisherController {
    @Autowired
    private PublisherService publisherService;
    @PostMapping
    public ApiResponse<PublisherResponse> createPublisher(@RequestBody PublisherRequest PublisherRequest) {
        ApiResponse<PublisherResponse> apiResponse = ApiResponse.<PublisherResponse>builder()
                .code(200)
                .message("Sucess")
                .result(publisherService.createPublisher(PublisherRequest))
                .build();
        return apiResponse;
    }
    @PutMapping
    public ApiResponse<PublisherResponse> updatePublisher(@RequestBody PublisherUpdate PublisherUpdate) {
        ApiResponse<PublisherResponse> apiResponse = ApiResponse.<PublisherResponse>builder()
                .code(200)
                .message("Sucess")
                .result(publisherService.updatePublisher(PublisherUpdate))
                .build();
        return apiResponse;
    }
    @GetMapping("/getall")
    public ApiResponse<List<PublisherResponse>> getAllPublishers() {
        ApiResponse<List<PublisherResponse>> apiResponse = ApiResponse.<List<PublisherResponse>>builder()
                .code(200)
                .message("Sucess")
                .result(publisherService.getAllPublisher())
                .build();
        return apiResponse;
    }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePublisher(@PathVariable int id) {
        publisherService.deletePublisher(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(200)
                .message("Publisher " + id + " is deleted")
                .build();
        return apiResponse;
    }
}
