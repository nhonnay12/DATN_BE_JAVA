package com.datn.mapper;

import com.datn.entity.Publisher;
import com.datn.dto.request.author_publisher.PublisherRequest;
import com.datn.dto.response.author_publisher.PublisherResponse;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
public class PublisherMapper {

    public Publisher toPublisher(PublisherRequest PublisherRequest) {
        if ( PublisherRequest == null ) {
            return null;
        }

        Publisher.PublisherBuilder publisher = Publisher.builder();

        publisher.name( PublisherRequest.getName() );
        publisher.address( PublisherRequest.getAddress() );
        publisher.status( PublisherRequest.getStatus() );

        return publisher.build();
    }

    public PublisherResponse toPublisherResponse(Publisher Publisher) {
        if ( Publisher == null ) {
            return null;
        }

        PublisherResponse.PublisherResponseBuilder publisherResponse = PublisherResponse.builder();

        if ( Publisher.getId() != null ) {
            publisherResponse.id( Publisher.getId() );
        }
        publisherResponse.name( Publisher.getName() );
        publisherResponse.address( Publisher.getAddress() );
        publisherResponse.status( Publisher.getStatus() );

        return publisherResponse.build();
    }
}
