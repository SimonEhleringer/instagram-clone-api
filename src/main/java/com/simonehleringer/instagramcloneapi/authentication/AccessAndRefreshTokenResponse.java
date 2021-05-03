package com.simonehleringer.instagramcloneapi.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessAndRefreshTokenResponse {
    private String accessToken;
    private String refreshToken;
}
