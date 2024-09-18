package com.datn.service.impl.author_publisher;



import com.datn.models.dto.request.author_publisher.AuthorRequest;
import com.datn.models.dto.request.author_publisher.AuthorUpdate;
import com.datn.models.dto.request.author_publisher.PublisherRequest;
import com.datn.models.dto.request.author_publisher.PublisherUpdate;
import com.datn.models.dto.response.author_publisher.AuthorResponse;
import com.datn.models.dto.response.author_publisher.PublisherResponse;
import com.datn.models.entity.Author;
import com.datn.models.entity.Publisher;
import com.datn.models.exception.AppException;
import com.datn.models.exception.ErrorCode;
import com.datn.models.mapper.AuthorMapper;
import com.datn.models.mapper.PublisherMapper;
import com.datn.repository.AuthorRepo;
import com.datn.repository.PublisherRepo;

import com.datn.service.AuthorService;
import com.datn.service.PublisherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    private AuthorRepo authorRepo;
    @Autowired
    private AuthorMapper authorMapper;
    @Override
    public AuthorResponse createAuthor(AuthorRequest request) {
        if(authorRepo.findByName(request.getName()).isPresent()){
            throw new AppException(ErrorCode.AUTHOR_EXISTED);
        }
        Author author = authorMapper.toAuthor(request);
        authorRepo.save(author);
        return authorMapper.toAuthorResponse(author);
    }

    @Override
    public List<AuthorResponse> getAllAuthor() {

        return authorRepo.findAll().stream().map(authorMapper::toAuthorResponse).toList();
    }

    @Override
    public AuthorResponse updateAuthor(AuthorUpdate request) {
        var author = authorRepo.findById(request.getId()).orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_EXISTED));
        author.setId(request.getId());
        author.setCountry(request.getCountry());
        authorRepo.save(author);
        return authorMapper.toAuthorResponse(author);
    }

    @Override
    public void deleteAuthor(int id) {
        authorRepo.deleteById(id);
    }
}
