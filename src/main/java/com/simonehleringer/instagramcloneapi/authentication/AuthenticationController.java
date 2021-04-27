package com.simonehleringer.instagramcloneapi.authentication;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/authentication")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return null;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        var test = authenticationService.register(
                registerRequest.getFullName(),
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getPassword()
        );

        return ResponseEntity
                .ok()
                .build();
    }
}
