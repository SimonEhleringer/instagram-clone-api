package com.simonehleringer.instagramcloneapi.authentication;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("Das eingegebene Passwort ist falsch.");
    }
}
