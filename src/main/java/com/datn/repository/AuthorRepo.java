package com.datn.repository;

import com.datn.models.entity.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface AuthorRepo extends JpaRepository<Author, Integer>, JpaSpecificationExecutor<Author> {
    Optional<Author> findByName(String name);
}
