package com.datn.service.impl;

import com.datn.models.entity.ImageData;
import com.datn.models.exception.AppException;
import com.datn.models.exception.ErrorCode;
import com.datn.repository.ImageRepo;
import com.datn.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepo imageRepo;
    private static String UPLOAD_DIR  = System.getProperty("user.dir") + "/src/main/resources/static/photos/";

    @Override
    public List<ImageData> getAllImage() {
        List<ImageData> images = imageRepo.findAll();

        return images;
    }

    @Override
    public ImageData getImageById(Long id) {
        ImageData image = imageRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_EXISTED));
        return image;
    }

    @Override
    public ImageData saveImage(MultipartFile file) {

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            if (!created) {
                throw new AppException(ErrorCode.DIRECTORY_CREATION_FAILED);
            }
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        if (originalFilename != null && originalFilename.length() > 0) {
            if (!extension.equals("png") && !extension.equals("jpg") && !extension.equals("JPG")
                    && !extension.equals("gif") && !extension.equals("svg")
                    && !extension.equals("jpeg")) {
                throw new AppException(ErrorCode.FILE_NOT_SUPPORT);
            }

            try {
                ImageData img = new ImageData();
                img.setName(file.getOriginalFilename());
                img.setSize(file.getSize());
                img.setType(extension);
                img.setImageData(file.getBytes());
                String uid = UUID.randomUUID().toString();
                String link = UPLOAD_DIR + uid + "." + extension;

                // Tạo file và ghi dữ liệu
                File serverFile = new File(link);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(file.getBytes());
                stream.close();

                // Lưu ảnh vào cơ sở dữ liệu
                imageRepo.save(img);
                return img;


            } catch (Exception e) {
                e.printStackTrace();
                throw new AppException(ErrorCode.FILE_NOT_UPLOAD);
            }
        }
        throw new AppException(ErrorCode.INVALID_FILE);
}

@Override
public void deleteImageById(Long id) {
    if (!imageRepo.findById(id).isPresent()) {
        throw new AppException(ErrorCode.IMAGE_NOT_EXISTED);
    }
    imageRepo.deleteById(id);
}
}
