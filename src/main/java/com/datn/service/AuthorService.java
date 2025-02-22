package com.datn.service;


import com.datn.models.dto.request.author_publisher.AuthorRequest;
import com.datn.models.dto.request.author_publisher.AuthorUpdate;
import com.datn.models.dto.response.author_publisher.AuthorResponse;

import java.util.List;

public interface AuthorService {
    AuthorResponse createAuthor(AuthorRequest request);
    List<AuthorResponse> getAllAuthor();
    AuthorResponse updateAuthor(AuthorUpdate request);
    void deleteAuthor(int id);
}
