package com.datn.models.dto.request.author_publisher;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorUpdate {
    private int id;
    private String country;
}
