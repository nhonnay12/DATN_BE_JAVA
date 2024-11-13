package com.datn.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name = "ImageData")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private Long size;

    @Lob
    @Column(name = "imagedata", columnDefinition = "LONGBLOB") // Change to LONGBLOB or BLOB
    private byte[] imageData;
    @ManyToOne
    @JoinColumn(name = "product_id")  // Tên cột khóa ngoại trong bảng ImageData
    @JsonIgnore
    private Product product;
    @ManyToOne
    @JoinColumn(name = "user_id")  // Tên cột khóa ngoại trong bảng ImageData
    @JsonIgnore
    private User user;
    private boolean hidden;
}
