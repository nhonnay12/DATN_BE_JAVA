package com.datn.service;




import com.datn.dto.request.author_publisher.PublisherRequest;
import com.datn.dto.request.author_publisher.PublisherUpdate;
import com.datn.dto.response.author_publisher.PublisherResponse;

import java.util.List;

public interface PublisherService {
    PublisherResponse createPublisher(PublisherRequest request);
    List<PublisherResponse> getAllPublisher();
    PublisherResponse updatePublisher(PublisherUpdate request);
    void deletePublisher(int id);
    public List<PublisherResponse> getAll();
}
