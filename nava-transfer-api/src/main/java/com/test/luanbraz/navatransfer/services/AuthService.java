package com.test.luanbraz.navatransfer.services;

import com.test.luanbraz.navatransfer.dto.SignupRequest;
import com.test.luanbraz.navatransfer.entities.Customer;

public interface AuthService {
    boolean createCustomer(SignupRequest request);
    Customer getCustomerByEmail(String email);
}
