package com.simonehleringer.instagramcloneapi.user;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/me/followed")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class MeFollowedController {
    public ResponseEntity<?> add(@RequestBody UserIdRequest request) {

    }

//    @GetMapping
//    public Response
}
