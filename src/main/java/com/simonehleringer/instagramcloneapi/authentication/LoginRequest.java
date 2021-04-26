package com.simonehleringer.instagramcloneapi.authentication;

import lombok.Data;

@Data
public class LoginRequest {
    // TODO: Validation
    private String usernameOrEmail;

    private String password;
}
