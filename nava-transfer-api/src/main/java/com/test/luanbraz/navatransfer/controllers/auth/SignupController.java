package com.test.luanbraz.navatransfer.controllers.auth;

import com.test.luanbraz.navatransfer.dto.SignupRequest;
import com.test.luanbraz.navatransfer.dto.errors.CustomErrorResponse;
import com.test.luanbraz.navatransfer.entities.Customer;
import com.test.luanbraz.navatransfer.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

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
                
                Customer createdCustomer = authService.getCustomerByEmail(request.getEmail());

                return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                        "id", createdCustomer.getId(),
                        "name", createdCustomer.getName(),
                        "email", createdCustomer.getEmail()
                ));
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
