package com.simonehleringer.instagramcloneapi.cloudinary;

public class CanNotDeleteImageException extends RuntimeException {
    public CanNotDeleteImageException() {
        super("Foto konnte nicht entfernt werden.");
    }
}
