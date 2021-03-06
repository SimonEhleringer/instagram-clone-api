package com.simonehleringer.instagramcloneapi.authentication.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {
    @NotBlank(message = "Es wurde kein Refresh-Token ├╝bermittelt.")
    private String refreshToken;
}
