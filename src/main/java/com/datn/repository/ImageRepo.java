package com.datn.repository;


import com.datn.models.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepo extends JpaRepository<ImageData, Long> {
void deleteByUserId(String userId);
}
