package com.simonehleringer.instagramcloneapi.user.me;

import com.simonehleringer.instagramcloneapi.common.ControllerUtils;
import com.simonehleringer.instagramcloneapi.user.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/me/suggestions")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class MeSuggestionsController {
    private final UserService userService;
    private final SuggestionsResponseMapper suggestionsResponseMapper;

    @GetMapping
    public ResponseEntity<?> getAll() {
        var optionalSuggestions = userService.getUsersSuggestions(ControllerUtils.getLoggedInUserId());

        var suggestions = optionalSuggestions.get();

        var response = suggestionsResponseMapper.toSuggestionsResponse(suggestions);

        return ResponseEntity.ok(response);
    }
}
