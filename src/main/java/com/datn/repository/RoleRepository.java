package com.datn.repository;

import com.datn.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(String roles);
}
