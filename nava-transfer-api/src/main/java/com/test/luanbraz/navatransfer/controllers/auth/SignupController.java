package com.test.luanbraz.navatransfer.controllers.auth;

import com.test.luanbraz.navatransfer.dto.SignupRequest;
import com.test.luanbraz.navatransfer.dto.errors.CustomErrorResponse;
import com.test.luanbraz.navatransfer.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/signup")
public class SignupController {

    private final AuthService authService;

    public SignupController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Object> signupCustomer(@RequestBody SignupRequest request) {
        try {
            boolean isUserCreated = authService.createCustomer(request);

            if (isUserCreated) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Customer registered successfully!");
            } else {
                CustomErrorResponse errorResponse = new CustomErrorResponse(
                        LocalDateTime.now(),
                        "Customer registration failed",
                        "A customer with the provided email already exists."
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
        } catch (Exception e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(
                    LocalDateTime.now(),
                    "Internal Server Error",
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
