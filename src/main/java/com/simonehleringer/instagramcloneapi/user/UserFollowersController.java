package com.simonehleringer.instagramcloneapi.user;

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
@RequestMapping("api/v1/users/{userId}/followers")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class UserFollowersController {
    private final UserService userService;
    private final FollowersResponseMapper followersResponseMapper;

    @GetMapping
    public ResponseEntity<?> getAll(@PathVariable UUID userId) {
        var optionalFollowers = userService.getUsersFollowers(userId);

        if (optionalFollowers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var followers = optionalFollowers.get();

        var response = followersResponseMapper.toFollowersResponse(followers);

        return ResponseEntity.ok(response);
    }
}
