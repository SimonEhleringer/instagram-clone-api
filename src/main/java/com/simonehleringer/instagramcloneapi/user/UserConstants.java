package com.simonehleringer.instagramcloneapi.user;

public class UserConstants {
    public static final int FULL_NAME__SIZE_MAX = 30;

    public static final int USERNAME__SIZE_MAX = 30;
    public static final String USERNAME__PATTERN_REGEXP = "^[a-zA-Z0-9_]*$";

    public static final int EMAIL__SIZE_MAX = 192;

    public static final int ENCODED_PASSWORD__SIZE_MAX = 60;

    public static final String PASSWORD__PATTERN_REGEXP = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,50}$";

    public static final int PUBLIC_PROFILE_IMAGE_ID__SIZE_MAX = 255;
}
