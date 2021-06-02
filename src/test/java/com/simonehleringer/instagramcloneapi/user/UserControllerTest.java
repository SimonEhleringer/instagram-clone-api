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

import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserResponseMapper userResponseMapper;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockAppUser
    void get_givenExistingUser_shouldReturnUserResponse() throws Exception {
        // Arrange
        var userId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        var user = new User();
        var optionalUser = Optional.of(user);

        var userResponse = new UserResponse(
                userId,
                "fullName",
                "username",
                "publicProfileImageId"
        );

        var expectedJson = objectMapper.writeValueAsString(userResponse);

        when(userService.getById(userId)).thenReturn(optionalUser);
        when(userResponseMapper.toUserResponse(user)).thenReturn(userResponse);

        // Act
        // Assert
        mockMvc.perform(
                get("/api/v1/users/" + userId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockAppUser
    void get_givenNotExistingUser_shouldReturnNotFound() throws Exception {
        // Arrange
        var userId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        given(userService.getById(userId)).willReturn(Optional.empty());

        // Act
        // Assert
        mockMvc.perform(
                get("/api/v1/users/" + userId.toString())
        )
                .andExpect(status().isNotFound());
    }
}