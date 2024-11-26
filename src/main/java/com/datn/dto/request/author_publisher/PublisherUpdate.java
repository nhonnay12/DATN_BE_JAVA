package com.datn.dto.request.author_publisher;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Filter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublisherUpdate {
    int id;
    String address;
    String phone;
}
