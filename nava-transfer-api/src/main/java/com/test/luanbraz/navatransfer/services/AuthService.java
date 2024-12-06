package com.test.luanbraz.navatransfer.services;

import com.test.luanbraz.navatransfer.dto.SignupRequest;

public interface AuthService {
    boolean createCustomer(SignupRequest request);
}
