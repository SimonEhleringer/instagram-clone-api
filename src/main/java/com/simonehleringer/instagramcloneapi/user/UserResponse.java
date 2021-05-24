package com.simonehleringer.instagramcloneapi.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserResponse {
    private UUID userId;

    private String fullName;

    private String username;

    private String publicProfileImageId;
}
