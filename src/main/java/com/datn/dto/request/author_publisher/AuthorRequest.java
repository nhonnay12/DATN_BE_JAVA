package com.datn.dto.request.author_publisher;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorRequest {
    private String name;
    private String country;
}

