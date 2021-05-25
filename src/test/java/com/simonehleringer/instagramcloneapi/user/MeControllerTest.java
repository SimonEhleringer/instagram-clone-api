package com.simonehleringer.instagramcloneapi.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonehleringer.instagramcloneapi.testUtil.annotation.WithMockAppUser;
import com.simonehleringer.instagramcloneapi.common.ControllerUtils;
import com.simonehleringer.instagramcloneapi.common.jwtAuthentication.JwtAuthenticationEntryPoint;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MeController.class)
@AutoConfigureMockMvc(addFilters = false)
class MeControllerTest {
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
        var user = new User();
        var optionalUser = Optional.of(user);

        var userResponse = new UserResponse(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "fullName",
                "username",
                "publicProfileImageId"
        );

        var expectedJson = objectMapper.writeValueAsString(userResponse);

        when(userService.getById(ControllerUtils.getLoggedInUserId())).thenReturn(optionalUser);
        when(userResponseMapper.toUserResponse(user)).thenReturn(userResponse);

        // Act
        // Assert
        mockMvc.perform(
                get("/api/v1/me")
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockAppUser
    void get_givenNotExistingUser_shouldReturnNotFound() throws Exception {
        // Arrange
        given(userService.getById(any())).willReturn(Optional.empty());

        // Act
        // Assert
        mockMvc.perform(
                get("/api/v1/me")
        )
                .andExpect(status().isNotFound());
    }
}