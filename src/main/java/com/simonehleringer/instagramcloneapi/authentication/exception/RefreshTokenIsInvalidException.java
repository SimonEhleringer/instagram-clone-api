package com.simonehleringer.instagramcloneapi.authentication.exception;

public class RefreshTokenIsInvalidException extends RuntimeException {
    public RefreshTokenIsInvalidException() {
        super("Das Refresh-Token ist invalide.");
    }
}
