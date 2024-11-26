package com.datn.mapper;

import com.datn.dto.request.author_publisher.AuthorRequest;
import com.datn.entity.Author;
import com.datn.dto.response.author_publisher.AuthorResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface AuthorMapper {

    Author toAuthor(AuthorRequest AuthorRequest);
    AuthorResponse toAuthorResponse(Author Author);
}
