package com.datn.dto.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponse {
    private Long id;
    private String name;
    private String type;
    private Long size;
    // Change imageData from byte[] to String for Base64 encoding
    private byte[] imageData;
}
