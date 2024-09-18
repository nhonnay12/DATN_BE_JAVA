package com.datn.models.dto.request.author_publisher;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublisherRequest {
    String name;
    String address;
    String phone;
}

