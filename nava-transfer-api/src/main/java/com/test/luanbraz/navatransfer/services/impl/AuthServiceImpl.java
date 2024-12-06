package com.test.luanbraz.navatransfer.services.impl;

import com.test.luanbraz.navatransfer.dto.SignupRequest;
import com.test.luanbraz.navatransfer.entities.Customer;
import com.test.luanbraz.navatransfer.repositories.CustomerRepository;
import com.test.luanbraz.navatransfer.services.AuthService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(CustomerRepository customerRepository, PasswordEncoder encoder) {
        this.customerRepository = customerRepository;
        this.encoder = encoder;
    }

    @Override
    public boolean createCustomer(SignupRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            return false;
        }
        Customer customer = new Customer();
        BeanUtils.copyProperties(request, customer);
        String hashPassword = encoder.encode(request.getPassword());
        customer.setPassword(hashPassword);
        customerRepository.save(customer);
        return true;
    }
}