package com.simonehleringer.instagramcloneapi.user;

import com.simonehleringer.instagramcloneapi.common.ControllerUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: No doubled follows
// TODO: Don't follow self
// TODO: Add tests

@RestController
@RequestMapping("api/v1/me/followed")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class MeFollowedController {
    private final UserService userService;
    private final FollowedResponseMapper followedResponseMapper;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody UserIdRequest request) {
        var optionalNewFollowed = userService.addFollow(ControllerUtils.getLoggedInUserId(), request.getUserId());

        if (optionalNewFollowed.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var newFollowed = optionalNewFollowed.get();

        var response = followedResponseMapper.toFollowedResponse(newFollowed);

        return ResponseEntity.ok(response);
    }

//    @GetMapping
//    public Response
}
