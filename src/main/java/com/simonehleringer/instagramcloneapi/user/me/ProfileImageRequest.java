package com.simonehleringer.instagramcloneapi.user.me;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileImageRequest {
    @NotNull(message = "Es wurde kein Profil-Bild übermittelt.")
    private String imageDataUri;
}
