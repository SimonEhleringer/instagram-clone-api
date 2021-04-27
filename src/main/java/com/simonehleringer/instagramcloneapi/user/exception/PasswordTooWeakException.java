package com.simonehleringer.instagramcloneapi.user.exception;

public class PasswordTooWeakException extends CanNotCreateUserException {
    public PasswordTooWeakException() {
        super("Das Passwort muss mindestens sechs Zeichen lang sein, einen Groß- und Kleinbuchstaben sowie eine Zahl und ein Sonderzeichen enthalten.");
    }
}
