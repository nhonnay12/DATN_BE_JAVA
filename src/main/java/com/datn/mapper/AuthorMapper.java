package com.datn.mapper;

import com.datn.dto.request.author_publisher.AuthorRequest;
import com.datn.dto.response.author_publisher.AuthorResponse;
import com.datn.entity.Author;
import org.springframework.stereotype.Component;

import javax.annotation.processing.Generated;
@Component
public class AuthorMapper {

    public Author toAuthor(AuthorRequest AuthorRequest) {
        if ( AuthorRequest == null ) {
            return null;
        }

        Author.AuthorBuilder author = Author.builder();

        author.name( AuthorRequest.getName() );
        author.address( AuthorRequest.getAddress() );
        author.status( AuthorRequest.getStatus() );

        return author.build();
    }

    public AuthorResponse toAuthorResponse(Author Author) {
        if ( Author == null ) {
            return null;
        }

        AuthorResponse.AuthorResponseBuilder authorResponse = AuthorResponse.builder();

        authorResponse.status( Author.getStatus() );
        if ( Author.getId() != null ) {
            authorResponse.id( Author.getId().longValue() );
        }
        authorResponse.name( Author.getName() );
        authorResponse.address( Author.getAddress() );

        return authorResponse.build();
    }
}
