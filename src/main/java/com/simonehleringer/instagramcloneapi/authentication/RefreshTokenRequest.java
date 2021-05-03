package com.simonehleringer.instagramcloneapi.authentication;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
