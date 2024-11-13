package com.datn.controller;

import com.datn.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping
public class ImageController {
    private static final String UPLOAD_DIR = "C:/Users/tdaov/OneDrive/Pictures/upload";

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload_file")
    @Operation(summary = "Upload file lên database")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        var response = imageService.saveImage(file);
        return ResponseEntity.ok(response);
    }
}
