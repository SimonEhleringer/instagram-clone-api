package com.simonehleringer.instagramcloneapi.post.me;

import com.simonehleringer.instagramcloneapi.common.ControllerUtils;
import com.simonehleringer.instagramcloneapi.post.PostRequest;
import com.simonehleringer.instagramcloneapi.post.PostResponseMapper;
import com.simonehleringer.instagramcloneapi.post.PostService;
import com.simonehleringer.instagramcloneapi.post.PostsResponseMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/me/posts")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class MePostController {
    private final PostService postService;
    private final PostResponseMapper postResponseMapper;
    private final PostsResponseMapper postsResponseMapper;

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody PostRequest request) {
        var optionalCreatedPost = postService.createPost(
                request.getText(),
                request.getImageDataUri(),
                ControllerUtils.getLoggedInUserId()
        );

        @SuppressWarnings("OptionalGetWithoutIsPresent")
        var createdPost = optionalCreatedPost.get();

        var response = postResponseMapper.toPostResponse(createdPost);

        return ResponseEntity
                .created(ControllerUtils.getLocationHeader(response.getPostId()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        var optionalPosts = postService.getAllUsersPosts(ControllerUtils.getLoggedInUserId());

        @SuppressWarnings("OptionalGetWithoutIsPresent")
        var posts = optionalPosts.get();

        var response = postsResponseMapper.toPostResponses(posts);

        return ResponseEntity.ok(response);
    }
}
