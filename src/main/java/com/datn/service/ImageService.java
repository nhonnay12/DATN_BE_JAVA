package com.datn.service;

import com.datn.entity.ImageData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ImageService {
List<ImageData> getAllImage();
ImageData getImageById(Long id);
ImageData saveImage(MultipartFile file);
void deleteImageById(Long id);
}
