package com.simonehleringer.instagramcloneapi.authentication;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RefreshTokenRequest {
    @NotBlank(message = "Es wurde kein Refresh-Token übermittelt.")
    private String refreshToken;
}
