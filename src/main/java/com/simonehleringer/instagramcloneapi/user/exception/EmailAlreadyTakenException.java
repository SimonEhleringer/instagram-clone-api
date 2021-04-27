package com.simonehleringer.instagramcloneapi.user.exception;

public class EmailAlreadyTakenException extends CanNotCreateUserException {
    public EmailAlreadyTakenException() {
        super("Diese E-Mail ist bereits vergeben.");
    }
}
