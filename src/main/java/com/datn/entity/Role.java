package com.datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
@Getter
@Setter
public class Role {
    @Id
    private String name;
    private String description;
    @JoinTable(
            name = "role_permissions", // Tên của bảng trung gian
            joinColumns = @JoinColumn(name = "roles_name"), // Tên cột khóa ngoại tham chiếu đến bảng Role
            inverseJoinColumns = @JoinColumn(name = "permission_name") // Tên cột khóa ngoại tham chiếu đến bảng Permission
    )
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    Set<Permission> permissions;
}
