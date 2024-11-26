package com.datn.service.impl.author_publisher;



import com.datn.dto.request.author_publisher.AuthorRequest;
import com.datn.dto.request.author_publisher.AuthorUpdate;
import com.datn.dto.response.author_publisher.AuthorResponse;
import com.datn.entity.Author;
import com.datn.exception.AppException;
import com.datn.exception.ErrorCode;
import com.datn.mapper.AuthorMapper;
import com.datn.repository.AuthorRepo;

import com.datn.service.AuthorService;
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
        author.setName(request.getName());
        author.setAddress(request.getAddress());
        author.setId(request.getId());
        author.setStatus(request.getStatus());
        authorRepo.save(author);
        return authorMapper.toAuthorResponse(author);
    }

    @Override
    public void deleteAuthor(int id) {
        Author author = authorRepo.findById(id).orElse(null);
        author.setStatus("INACTIVE");
        authorRepo.save(author);
    }
}
