package com.datn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
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
    @Column(name = "email", unique = true, columnDefinition
            = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    private String email;
    private String phone;
    @ManyToMany
    @JoinTable(
            name = "user_roles", // Tên của bảng trung gian
            joinColumns = @JoinColumn(name = "user_id"), // Tên cột khóa ngoại tham chiếu đến bảng User
            inverseJoinColumns = @JoinColumn(name = "roles_name") // Tên cột khóa ngoại tham chiếu đến bảng Role
    )
    Set<Role> roles;
    @OneToMany(mappedBy = "user")
    private List<ImageData> images;
    private String status;

    //constructor for creating an unverified user
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> carts = new ArrayList<>();

}
