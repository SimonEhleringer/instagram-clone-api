package com.simonehleringer.instagramcloneapi.cloudinary;

import lombok.Getter;

@Getter
public enum ImageType {
    POST("post"),
    PROFILE_IMAGE("profile-image");

    private final String folderName;

    ImageType(String folderName) {
        this.folderName = folderName;
    }
}
