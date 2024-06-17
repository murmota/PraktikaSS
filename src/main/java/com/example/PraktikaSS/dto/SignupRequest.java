package com.example.PraktikaSS.dto;

import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    private String userName;
    private String email;
    private String password;
    private Set<String> roles;
    private String phoneNumber;
}
