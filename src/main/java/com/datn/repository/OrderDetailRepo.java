package com.datn.repository;

import com.datn.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface OrderDetailRepo extends JpaRepository<OrderDetail, Long> {

}
