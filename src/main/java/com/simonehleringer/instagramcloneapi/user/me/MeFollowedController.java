package com.simonehleringer.instagramcloneapi.user.me;

import com.simonehleringer.instagramcloneapi.common.ControllerUtils;
import com.simonehleringer.instagramcloneapi.user.FollowedResponseMapper;
import com.simonehleringer.instagramcloneapi.user.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/me/followed")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class MeFollowedController {
    private final UserService userService;
    private final FollowedResponseMapper followedResponseMapper;

    @PostMapping(path = "{followedId}")
    public ResponseEntity<?> add(@PathVariable UUID followedId) {
        var hasBeenAdded = userService.addFollow(ControllerUtils.getLoggedInUserId(), followedId);

        if (!hasBeenAdded) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        var optionalFollowed = userService.getUsersFollowed(ControllerUtils.getLoggedInUserId());

        if (optionalFollowed.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var followed = optionalFollowed.get();

        var response = followedResponseMapper.toFollowedResponse(followed);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "{followedId}")
    public ResponseEntity<?> delete(@PathVariable UUID followedId) {
        var hasBeenRemoved = userService.removeFollow(ControllerUtils.getLoggedInUserId(), followedId);

        if (!hasBeenRemoved) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
