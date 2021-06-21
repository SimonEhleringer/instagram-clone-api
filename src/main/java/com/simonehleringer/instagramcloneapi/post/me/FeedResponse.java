package com.simonehleringer.instagramcloneapi.post.me;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedResponse {
    private List<FeedPostResponse> feed;
}
