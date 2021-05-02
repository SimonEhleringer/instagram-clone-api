package com.simonehleringer.instagramcloneapi.authentication;

public class UsernameOrEmailNotFoundException extends RuntimeException {
    public UsernameOrEmailNotFoundException() {
        super("Der Benutzername bzw. die Email ist keinem Konto zugeordnet.");
    }
}
