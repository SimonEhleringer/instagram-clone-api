package com.simonehleringer.instagramcloneapi.user.me;

public class CanNotAddFollowException extends RuntimeException {
    public CanNotAddFollowException(String message) {
        super(message);
    }
}
