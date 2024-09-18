package com.datn.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "username", unique = true, columnDefinition
            = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    @ManyToMany
    @JoinTable(
            name = "user_roles", // Tên của bảng trung gian
            joinColumns = @JoinColumn(name = "user_id"), // Tên cột khóa ngoại tham chiếu đến bảng User
            inverseJoinColumns = @JoinColumn(name = "roles_name") // Tên cột khóa ngoại tham chiếu đến bảng Role
    )
    Set<Role> roles;

}
