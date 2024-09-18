package com.datn.controller.product;


import com.datn.models.dto.request.author_publisher.AuthorRequest;
import com.datn.models.dto.request.author_publisher.AuthorUpdate;
import com.datn.models.dto.response.ApiResponse;
import com.datn.models.dto.response.author_publisher.AuthorResponse;
import com.datn.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/author")
public class AuthorController {
    @Autowired
    private AuthorService authorService;
    @PostMapping
    public ApiResponse<AuthorResponse> createAuthor(@RequestBody AuthorRequest AuthorRequest) {
        ApiResponse<AuthorResponse> apiResponse = ApiResponse.<AuthorResponse>builder()
                .code(200)
                .message("Sucess")
                .result(authorService.createAuthor(AuthorRequest))
                .build();
        return apiResponse;
    }
    @PutMapping
    public ApiResponse<AuthorResponse> updateAuthor(@RequestBody AuthorUpdate AuthorUpdate) {
        ApiResponse<AuthorResponse> apiResponse = ApiResponse.<AuthorResponse>builder()
                .code(200)
                .message("Sucess")
                .result(authorService.updateAuthor(AuthorUpdate))
                .build();
        return apiResponse;
    }
    @GetMapping
    public ApiResponse<List<AuthorResponse>> getAllAuthors() {
        ApiResponse<List<AuthorResponse>> apiResponse = ApiResponse.<List<AuthorResponse>>builder()
                .code(200)
                .message("Sucess")
                .result(authorService.getAllAuthor())
                .build();
        return apiResponse;
    }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAuthor(@PathVariable int id) {
        authorService.deleteAuthor(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(200)
                .message("Author " + id + " is deleted")
                .build();
        return apiResponse;
    }
}
