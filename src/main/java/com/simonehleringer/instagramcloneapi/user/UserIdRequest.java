package com.simonehleringer.instagramcloneapi.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UserIdRequest {
    private UUID userId;
}
