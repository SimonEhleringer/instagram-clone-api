package com.simonehleringer.instagramcloneapi.authentication;

import lombok.Data;

@Data
public class RegisterRequest {
    // TODO: Validation
    private String email;

    private String fullName;

    private String username;

    private String password;
}
