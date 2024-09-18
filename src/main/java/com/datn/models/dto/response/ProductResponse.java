package com.datn.models.dto.response;

import com.datn.models.entity.*;
import lombok.*;

import java.util.List;
import java.util.Set;

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
    private Set<Author> authors;
    private Publisher publisher;
}
