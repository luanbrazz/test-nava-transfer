package com.test.luanbraz.navatransfer.controllers.auth;

import com.test.luanbraz.navatransfer.dto.SignupRequest;
import com.test.luanbraz.navatransfer.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
public class SignupController {
    private final AuthService authService;
    public SignupController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping
    public ResponseEntity<String> signupCustomer(@RequestBody SignupRequest request) {
        boolean isUserCreated = authService.createCustomer(request);
        if (isUserCreated){
            return ResponseEntity.status(HttpStatus.CREATED).body("Customer registered successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to register customer!");
        }
    }
}
