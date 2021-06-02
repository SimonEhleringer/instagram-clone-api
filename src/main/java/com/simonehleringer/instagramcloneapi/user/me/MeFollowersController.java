package com.simonehleringer.instagramcloneapi.user.me;

import com.simonehleringer.instagramcloneapi.common.ControllerUtils;
import com.simonehleringer.instagramcloneapi.user.FollowersResponseMapper;
import com.simonehleringer.instagramcloneapi.user.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/me/followers")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class MeFollowersController {
    private final UserService userService;
    private final FollowersResponseMapper followersResponseMapper;

    @GetMapping
    public ResponseEntity<?> getAll() {
        var optionalFollowers = userService.getUsersFollowers(ControllerUtils.getLoggedInUserId());

        @SuppressWarnings("OptionalGetWithoutIsPresent")
        var followers = optionalFollowers.get();

        var response = followersResponseMapper.toFollowersResponse(followers);

        return ResponseEntity.ok(response);
    }
}
