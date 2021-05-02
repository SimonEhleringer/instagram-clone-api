package com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessAndRefreshToken {
    private String accessToken;
    private String refreshToken;
}
