package com.simonehleringer.instagramcloneapi.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonehleringer.instagramcloneapi.common.jwtAuthentication.JwtAuthenticationEntryPoint;
import com.simonehleringer.instagramcloneapi.testUtil.annotation.WithMockAppUser;
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

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserFollowersController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserFollowersControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private FollowersResponseMapper followersResponseMapper;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private UserResponseMapper userResponseMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockAppUser()
    void getAll_givenExistingUser_shouldReturnUsersFollowers() throws Exception {
        // Arrange
        var userId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        var followersList = new ArrayList<User>();
        var followersResponseList = new ArrayList<UserResponse>();

        followersResponseList.add(new UserResponse(
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                "fullName",
                "username",
                "publicProfileImageId"
        ));

        var expectedResponse = new FollowersResponse(followersResponseList);
        var expectedJson = objectMapper.writeValueAsString(expectedResponse);

        given(userService.getUsersFollowers(userId)).willReturn(Optional.of(followersList));
        given(followersResponseMapper.toFollowersResponse(followersList)).willReturn(expectedResponse);

        // Act
        // Assert
        mockMvc.perform(
                get("/api/v1/users/" + userId + "/followers")
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockAppUser()
    void getAll_givenNotExistingUser_shouldReturnNotFound() throws Exception {
        // Arrange
        var userId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        given(userService.getUsersFollowers(userId)).willReturn(Optional.empty());

        // Act
        // Assert
        mockMvc.perform(
                get("/api/v1/users/" + userId + "/followers")
        )
                .andExpect(status().isNotFound());
    }
}