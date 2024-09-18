package com.datn.service;




import com.datn.models.dto.request.author_publisher.PublisherRequest;
import com.datn.models.dto.request.author_publisher.PublisherUpdate;
import com.datn.models.dto.response.author_publisher.PublisherResponse;

import java.util.List;

public interface PublisherService {
    PublisherResponse createPublisher(PublisherRequest request);
    List<PublisherResponse> getAllPublisher();
    PublisherResponse updatePublisher(PublisherUpdate request);
    void deletePublisher(int id);
}
