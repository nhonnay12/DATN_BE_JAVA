package com.datn.models.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
@Getter
@Setter
public class Permission {
    @Id
    private String name;
    private String description;
}
