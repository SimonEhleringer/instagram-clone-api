package com.simonehleringer.instagramcloneapi.user;

public class CanNotCreateUserException extends RuntimeException {
    public CanNotCreateUserException(String message) {
        super(message);
    }
}
