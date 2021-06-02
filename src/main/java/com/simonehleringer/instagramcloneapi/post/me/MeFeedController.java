package com.simonehleringer.instagramcloneapi.post.me;

import com.simonehleringer.instagramcloneapi.common.ControllerUtils;
import com.simonehleringer.instagramcloneapi.post.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/me/feed")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class MeFeedController {
    private final PostService postService;
    private final FeedResponseMapper feedResponseMapper;

    @GetMapping
    public ResponseEntity<?> get() {
        var optionalFeed = postService.getUsersFeed(ControllerUtils.getLoggedInUserId());

        var feed = optionalFeed.get();

        var response = feedResponseMapper.toFeedResponse(feed);

        return ResponseEntity.ok(response);
    }
}
