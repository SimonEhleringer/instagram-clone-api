package com.simonehleringer.instagramcloneapi.authentication;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/authentication")
@AllArgsConstructor
public class AuthenticationController {
    // TODO: Write tests
    private final AuthenticationService authenticationService;

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        var accessAndRefreshToken = authenticationService.login(
                loginRequest.getUsernameOrEmail(),
                loginRequest.getPassword()
        );

        var response = AccessAndRefreshTokenResponseMapper.MAPPER
                .toAccessAndRefreshTokenResponse(accessAndRefreshToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        var accessAndRefreshToken = authenticationService.register(
                registerRequest.getFullName(),
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getPassword()
        );

        var response = AccessAndRefreshTokenResponseMapper.MAPPER
                .toAccessAndRefreshTokenResponse(accessAndRefreshToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/refreshAccessToken")
    public ResponseEntity<?> refreshAccessToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        var accessAndRefreshToken = authenticationService.refreshAccessToken(
                refreshTokenRequest.getRefreshToken()
        );

        var response = AccessAndRefreshTokenResponseMapper.MAPPER
                .toAccessAndRefreshTokenResponse(accessAndRefreshToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        authenticationService.logout(
                refreshTokenRequest.getRefreshToken()
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}
