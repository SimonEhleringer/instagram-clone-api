package com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessAndRefreshToken {
    private String accessToken;
    private String refreshToken;
}
