package com.simonehleringer.instagramcloneapi.authentication.exception;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("Das eingegebene Passwort ist falsch.");
    }
}
