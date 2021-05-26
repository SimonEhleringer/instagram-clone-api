package com.simonehleringer.instagramcloneapi.cloudinary;

public class CanNotUploadImageException extends RuntimeException {
    public CanNotUploadImageException() {
        super("Foto konnte nicht hochgeladen werden.");
    }
}
