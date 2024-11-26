package com.datn.dto.response;

import com.datn.entity.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse{
    private Long id;
    private String name;
    private String description;
    private Double price;
    private int quantity;
    private boolean status;
    private List<ImageData> images;
    private Category category;
    private Author author;
    private Publisher publisher;
    private User user;
    private String linkDrive;
    ProductResponse(String name, String linkDrive) {
        this.name = name;
        this.linkDrive = linkDrive;
    }
}
