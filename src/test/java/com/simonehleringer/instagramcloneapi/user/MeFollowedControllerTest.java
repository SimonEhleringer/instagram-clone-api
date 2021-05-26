package com.simonehleringer.instagramcloneapi.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonehleringer.instagramcloneapi.common.jwtAuthentication.JwtAuthenticationEntryPoint;
import com.simonehleringer.instagramcloneapi.testUtil.annotation.WithMockAppUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MeFollowedController.class)
@AutoConfigureMockMvc(addFilters = false)
class MeFollowedControllerTest {
    private final String loggedInUserIdAsString = "11111111-1111-1111-1111-111111111111";
    private final UUID loggedInUserId = UUID.fromString(loggedInUserIdAsString);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private FollowedResponseMapper followedResponseMapper;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private UserResponseMapper userResponseMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockAppUser(userIdAsString = loggedInUserIdAsString)
    void add_givenExistingFollowerAndExistingFollowed_shouldReturn() throws Exception {
        // Arrange
        var followedId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        var fullName = "fullName";
        var username = "username";
        var publicProfileImageId = "publicProfileImageId";

        var userResponse = new UserResponse(
                followedId,
                fullName,
                username,
                publicProfileImageId
        );

        var userResponseList = new ArrayList<UserResponse>();
        userResponseList.add(userResponse);

        var expectedResponse = new FollowedResponse(
            userResponseList
        );

        var expectedResponseAsJson = objectMapper.writeValueAsString(expectedResponse);

        var followedList = new ArrayList<User>();
        followedList.add(new User());

        given(userService.addFollow(loggedInUserId, followedId)).willReturn(Optional.of(followedList));
        given(followedResponseMapper.toFollowedResponse(followedList)).willReturn(expectedResponse);

        // Act
        // Assert
        mockMvc.perform(
                post("/api/v1/me/followed/" + followedId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseAsJson));

        verify(userService).addFollow(loggedInUserId, followedId);
    }

    @Test
    @WithMockAppUser(userIdAsString = loggedInUserIdAsString)
    void add_givenNotExistingFollowerIdOrNotExistingFollowedId_shouldReturnNotFound() throws Exception {
        // Arrange
        var followedId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        given(userService.addFollow(loggedInUserId, followedId)).willReturn(Optional.empty());

        // Act
        // Assert
        mockMvc.perform(
                post("/api/v1/me/followed" + followedId.toString())
        )
                .andExpect(status().isNotFound());
    }
}