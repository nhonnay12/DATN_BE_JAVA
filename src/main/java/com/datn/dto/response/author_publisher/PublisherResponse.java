package com.datn.dto.response.author_publisher;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublisherResponse {
    int id;
    String name;
    String address;
    String status;
}
