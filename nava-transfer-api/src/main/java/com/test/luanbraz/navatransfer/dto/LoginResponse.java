package com.test.luanbraz.navatransfer.dto;

public class LoginResponse {
    private String jwtToken;
    public LoginResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }
    public String getJwtToken() {
        return jwtToken;
    }
}
