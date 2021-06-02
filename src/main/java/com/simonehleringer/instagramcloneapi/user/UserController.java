package com.simonehleringer.instagramcloneapi.user;

import com.simonehleringer.instagramcloneapi.common.ControllerUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/users/{userId}")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserResponseMapper userResponseMapper;

    @GetMapping
    public ResponseEntity<?> get(@PathVariable UUID userId) {
        var optionalUser = userService.getById(userId);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var user = optionalUser.get();

        var response = userResponseMapper.toUserResponse(user);

        return ResponseEntity.ok(response);
    }
}
