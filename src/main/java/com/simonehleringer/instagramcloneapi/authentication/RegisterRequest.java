package com.simonehleringer.instagramcloneapi.authentication;

import lombok.Data;

import javax.validation.constraints.*;

import static com.simonehleringer.instagramcloneapi.user.UserConstants.*;

@Data
public class RegisterRequest {
    @NotBlank(message = "Es wurde keine E-Mail Adresse übermittelt.")
    @Email(message = "Die E-Mail Adresse ist ungültig.")
    private String email;

    @NotNull(message = "Es wurde kein vollständiger Name übermittelt.")
    @Size(max = FULL_NAME__SIZE_MAX, message = "Der vollständige Name darf höchstens " + FULL_NAME__SIZE_MAX + " Zeichen lang sein.")
    private String fullName;

    @NotBlank(message = "Es wurde kein Benutzername übermittelt.")
    @Size(max = USERNAME__SIZE_MAX, message = "Der Benutzername darf höchstens " + USERNAME__SIZE_MAX + " Zeichen lang sein.")
    // Only characters, numbers and underscore
    @Pattern(regexp = USERNAME__PATTERN_REGEXP, message = "Der Benutzername darf nur Groß- und Kleinbuchstaben sowie den Unterstrich enthalten.")
    private String username;

    @NotNull(message = "Es wurde kein Passwort übermittelt.")
    @Pattern(regexp = PASSWORD__PATTERN_REGEXP, message = "Das Passwort muss zwischen 6 und 50 Zeichen lang sein, einen Groß- und Kleinbuchstaben sowie eine Zahl und ein Sonderzeichen enthalten.")
    private String password;
}
