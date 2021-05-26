package com.simonehleringer.instagramcloneapi.user;

import com.simonehleringer.instagramcloneapi.common.ControllerUtils;
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
        var optionalNewFollowed = userService.addFollow(ControllerUtils.getLoggedInUserId(), followedId);

        if (optionalNewFollowed.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var newFollowed = optionalNewFollowed.get();

        var response = followedResponseMapper.toFollowedResponse(newFollowed);

        return ResponseEntity.ok(response);
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
        var optionalNewFollowed = userService.removeFollow(ControllerUtils.getLoggedInUserId(), followedId);

        if (optionalNewFollowed.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var newFollowed = optionalNewFollowed.get();

        var response = followedResponseMapper.toFollowedResponse(newFollowed);

        return ResponseEntity.ok(response);
    }
}
