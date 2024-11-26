package com.datn.mapper;

import com.datn.entity.Publisher;
import com.datn.dto.request.author_publisher.PublisherRequest;
import com.datn.dto.response.author_publisher.PublisherResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface PublisherMapper {
    Publisher toPublisher(PublisherRequest PublisherRequest);
    PublisherResponse toPublisherResponse(Publisher Publisher);
}
