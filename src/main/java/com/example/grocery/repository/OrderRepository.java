package com.example.grocery.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.grocery.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
