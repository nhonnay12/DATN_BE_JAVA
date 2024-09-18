package com.datn.models.mapper;

import com.datn.models.dto.request.author_publisher.AuthorRequest;
import com.datn.models.dto.response.author_publisher.AuthorResponse;
import com.datn.models.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface AuthorMapper {

    Author toAuthor(AuthorRequest AuthorRequest);
    AuthorResponse toAuthorResponse(Author Author);
}
