package com.simonehleringer.instagramcloneapi.user;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/users/{userId}/followed")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class UserFollowedController {
    private final UserService userService;
    private final FollowedResponseMapper followedResponseMapper;

    @GetMapping
    public ResponseEntity<?> getAll(@PathVariable UUID userId) {
        var optionalFollowed = userService.getUsersFollowed(userId);

        if (optionalFollowed.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var followed = optionalFollowed.get();

        var response = followedResponseMapper.toFollowedResponse(followed);

        return ResponseEntity.ok(response);
    }
}
