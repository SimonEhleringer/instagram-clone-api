package com.simonehleringer.instagramcloneapi.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Integer postId;

    private String publicImageId;

    private String text;

    private LocalDateTime creationTime;
}
