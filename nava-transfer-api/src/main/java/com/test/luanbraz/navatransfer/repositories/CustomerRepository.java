package com.test.luanbraz.navatransfer.repositories;

import com.test.luanbraz.navatransfer.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);
    Optional<Customer> findByEmail(String email);
}