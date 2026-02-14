package com.journal.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;

    private String type = "Bearer";

    private String id;

    private String username;

    private String email;

    private Set<String> roles;

    public AuthResponse(String token, String id, String username, String email, Set<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}