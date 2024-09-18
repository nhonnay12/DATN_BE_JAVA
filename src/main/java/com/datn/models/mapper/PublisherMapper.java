package com.datn.models.mapper;

import com.datn.models.dto.request.author_publisher.PublisherRequest;
import com.datn.models.dto.response.author_publisher.PublisherResponse;
import com.datn.models.entity.Publisher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface PublisherMapper {
    Publisher toPublisher(PublisherRequest PublisherRequest);
    PublisherResponse toPublisherResponse(Publisher Publisher);
}
