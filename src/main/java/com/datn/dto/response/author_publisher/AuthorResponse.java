package com.datn.dto.response.author_publisher;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorResponse {
    private Long id;
    private String name;
    private String country;
}
