package com.simonehleringer.instagramcloneapi.post;

import com.simonehleringer.instagramcloneapi.common.ControllerUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/users/{userId}/posts")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class UserPostController {
    private final PostService postService;
    private final PostsResponseMapper postsResponseMapper;

    @GetMapping
    public ResponseEntity<?> getAll(@PathVariable UUID userId) {
        var optionalPosts = postService.getAllUsersPosts(userId);

        if (optionalPosts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var posts = optionalPosts.get();

        var response = postsResponseMapper.toPostResponses(posts);

        return ResponseEntity.ok(response);
    }
}
