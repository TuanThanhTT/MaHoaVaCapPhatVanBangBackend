package com.certicrypt.certicrypt.DTO.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String role;
    private boolean isStudent;
}
