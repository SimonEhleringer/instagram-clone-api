package com.simonehleringer.instagramcloneapi.user;

public class CanNotAddFollowException extends RuntimeException {
    public CanNotAddFollowException(String message) {
        super(message);
    }
}
