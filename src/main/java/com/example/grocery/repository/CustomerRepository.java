package com.example.grocery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.grocery.entities.Customer;

@Repository // Marks this interface as a Spring Data JPA repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // JpaRepository provides out-of-the-box CRUD methods: save, findById, findAll, deleteById, etc.
    // You can add custom query methods here if needed, e.g.,
    // Optional<Customer> findByEmail(String email);
}