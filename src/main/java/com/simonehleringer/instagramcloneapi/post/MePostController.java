package com.simonehleringer.instagramcloneapi.post;

import com.simonehleringer.instagramcloneapi.common.ControllerUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/me/posts")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class MePostController {
    private final PostService postService;
    private final PostResponseMapper postResponseMapper;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody PostRequest request) {
        var optionalCreatedPost = postService.createPost(
                request.getText(),
                request.getImageDataUri(),
                ControllerUtils.getLoggedInUserId()
        );

        // TODO: Maybe no if check?
        if (optionalCreatedPost.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var createdPost = optionalCreatedPost.get();

        var response = postResponseMapper.toPostResponse(createdPost);

        return ResponseEntity
                .created(ControllerUtils.getLocationHeader(response.getPostId()))
                .body(response);
    }
}
