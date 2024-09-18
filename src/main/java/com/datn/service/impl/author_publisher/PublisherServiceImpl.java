package com.datn.service.impl.author_publisher;

import com.datn.models.dto.request.author_publisher.PublisherRequest;
import com.datn.models.dto.request.author_publisher.PublisherUpdate;
import com.datn.models.dto.response.author_publisher.PublisherResponse;
import com.datn.models.entity.Publisher;
import com.datn.models.exception.AppException;
import com.datn.models.exception.ErrorCode;
import com.datn.models.mapper.PublisherMapper;
import com.datn.repository.PublisherRepo;

import com.datn.service.PublisherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class PublisherServiceImpl implements PublisherService {
    @Autowired
    private PublisherRepo publisherRepo;
    @Autowired
    private PublisherMapper publisherMapper;

    @Override
    public PublisherResponse createPublisher(PublisherRequest request) {
        if(publisherRepo.findByName(request.getName()).isPresent()){
            throw new AppException(ErrorCode.PUBLISHER_EXISTED);
        }

        Publisher publisher = publisherMapper.toPublisher(request);
        publisherRepo.save(publisher);
        return publisherMapper.toPublisherResponse(publisher);
    }

    @Override
    public List<PublisherResponse> getAllPublisher() {

        return publisherRepo.findAll().stream().map(publisherMapper::toPublisherResponse).toList();
    }

    @Override
    public PublisherResponse updatePublisher(PublisherUpdate request) {
        var publisher = publisherRepo.findById(request.getId()).orElseThrow(() -> new AppException(ErrorCode.PUBLISHER_NOT_EXISTED));
        publisher.setId(request.getId());
        publisher.setPhone(request.getPhone());
        publisher.setAddress(request.getAddress());

        publisherRepo.save(publisher);
        return publisherMapper.toPublisherResponse(publisher);
    }

    @Override
    public void deletePublisher(int id) {
        publisherRepo.deleteById(id);
    }
}