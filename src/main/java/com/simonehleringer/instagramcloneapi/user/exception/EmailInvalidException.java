package com.simonehleringer.instagramcloneapi.user.exception;

public class EmailInvalidException extends CanNotCreateUserException {
    public EmailInvalidException() {
        super("Diese E-Mail ist nicht valide.");
    }
}
