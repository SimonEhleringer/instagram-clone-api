package com.simonehleringer.instagramcloneapi.post.me;

import com.simonehleringer.instagramcloneapi.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedPostResponse {
    private Integer postId;

    private String publicImageId;

    private String text;

    private LocalDateTime creationTime;

    private UserResponse creator;
}
