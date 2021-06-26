package com.simonehleringer.instagramcloneapi.user.me;

import com.simonehleringer.instagramcloneapi.common.ControllerUtils;
import com.simonehleringer.instagramcloneapi.user.UserResponseMapper;
import com.simonehleringer.instagramcloneapi.user.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/me/profile-image")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class MeProfileImageController {
    private final UserService userService;
    private final UserResponseMapper userResponseMapper;

    @PostMapping
    public ResponseEntity<?> changeProfileImage(@RequestBody ProfileImageRequest request) {
        var optionalUser =
                userService.changeProfileImage(ControllerUtils.getLoggedInUserId(), request.getImageDataUri());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var user = optionalUser.get();

        var response = userResponseMapper.toUserResponse(user);

        return ResponseEntity.ok(response);
    }
}
