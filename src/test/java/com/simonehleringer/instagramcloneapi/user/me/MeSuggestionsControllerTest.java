package com.simonehleringer.instagramcloneapi.user.me;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonehleringer.instagramcloneapi.common.jwtAuthentication.JwtAuthenticationEntryPoint;
import com.simonehleringer.instagramcloneapi.testUtil.annotation.WithMockAppUser;
import com.simonehleringer.instagramcloneapi.user.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("DefaultAnnotationParam")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MeSuggestionsController.class)
@AutoConfigureMockMvc(addFilters = false)
class MeSuggestionsControllerTest {
    private final String loggedInUserIdAsString = "11111111-1111-1111-1111-111111111111";
    private final UUID loggedInUserId = UUID.fromString(loggedInUserIdAsString);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private SuggestionsResponseMapper suggestionsResponseMapper;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private UserResponseMapper userResponseMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockAppUser(userIdAsString = loggedInUserIdAsString)
    void getAll_shouldReturnUsersSuggestions() throws Exception {
        // Arrange
        var suggestionsList = new ArrayList<User>();
        var suggestionsResponseList = new ArrayList<UserResponse>();

        suggestionsResponseList.add(new UserResponse(
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                "fullName",
                "username",
                "publicProfileImageId"
        ));

        var expectedResponse = new SuggestionsResponse(suggestionsResponseList);
        var expectedJson = objectMapper.writeValueAsString(expectedResponse);

        given(userService.getUsersSuggestions(loggedInUserId)).willReturn(Optional.of(suggestionsList));
        given(suggestionsResponseMapper.toSuggestionsResponse(suggestionsList)).willReturn(expectedResponse);

        // Act
        // Assert
        mockMvc.perform(
                get("/api/v1/me/suggestions")
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }
}