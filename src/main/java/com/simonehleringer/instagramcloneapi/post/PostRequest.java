package com.simonehleringer.instagramcloneapi.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.simonehleringer.instagramcloneapi.post.PostConstants.TEXT__SIZE_MAX;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    @NotNull(message = "Es wurde kein Bild übermittelt.")
    private String imageDataUri;

    @Size(max = TEXT__SIZE_MAX, message = "Der Text darf maximal " + TEXT__SIZE_MAX + " Zeichen lang sein.")
    private String text;
}
