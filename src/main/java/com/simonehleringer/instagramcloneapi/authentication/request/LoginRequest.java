package com.simonehleringer.instagramcloneapi.authentication.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Es wurde kein Benutzername / keine E-Mail ├╝bermittelt.")
    private String usernameOrEmail;

    @NotBlank(message = "Es wurde kein Passwort ├╝bermittelt.")
    private String password;
}
