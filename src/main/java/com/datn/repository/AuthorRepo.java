package com.datn.repository;

import com.datn.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepo extends JpaRepository<Author, Integer>, JpaSpecificationExecutor<Author> {
    Optional<Author> findByName(String name);
}
