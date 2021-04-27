package com.simonehleringer.instagramcloneapi.user.exception;

public class UsernameAlreadyTakenException extends CanNotCreateUserException {
    public UsernameAlreadyTakenException() {
        super("Dieser Benutzername ist bereits vergeben.");
    }
}
