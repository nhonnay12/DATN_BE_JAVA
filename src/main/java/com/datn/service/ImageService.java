package com.datn.service;

import com.datn.models.entity.ImageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ImageService {
List<ImageData> getAllImage();
ImageData getImageById(Long id);
ImageData saveImage(MultipartFile file);
void deleteImageById(Long id);
}
