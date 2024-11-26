package com.datn.repository;

import com.datn.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublisherRepo extends JpaRepository<Publisher, Integer> {
    Optional<Publisher> findByName(String name);
}
