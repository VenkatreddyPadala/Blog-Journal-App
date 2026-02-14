package com.journal.app.controller;

import com.journal.app.dto.request.LoginRequest;
import com.journal.app.dto.request.RegisterRequest;
import com.journal.app.dto.response.AuthResponse;
import com.journal.app.dto.response.MessageResponse;
import com.journal.app.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        MessageResponse response = authService.register(registerRequest);

        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }
}