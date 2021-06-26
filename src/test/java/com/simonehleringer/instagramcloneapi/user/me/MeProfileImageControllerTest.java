package com.simonehleringer.instagramcloneapi.user.me;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonehleringer.instagramcloneapi.common.ErrorResponse;
import com.simonehleringer.instagramcloneapi.common.jwtAuthentication.JwtAuthenticationEntryPoint;
import com.simonehleringer.instagramcloneapi.testUtil.annotation.WithMockAppUser;
import com.simonehleringer.instagramcloneapi.user.User;
import com.simonehleringer.instagramcloneapi.user.UserResponse;
import com.simonehleringer.instagramcloneapi.user.UserResponseMapper;
import com.simonehleringer.instagramcloneapi.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("DefaultAnnotationParam")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MeProfileImageController.class)
@AutoConfigureMockMvc(addFilters = false)
class MeProfileImageControllerTest {
    private final String MOCKED_UUID_AS_STRING = "11111111-1111-1111-1111-111111111111";
    private final UUID MOCKED_UUID = UUID.fromString(MOCKED_UUID_AS_STRING);

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
    @WithMockAppUser(userIdAsString = MOCKED_UUID_AS_STRING)
    void changeProfileImage_givenExistingUser_shouldChangeProfileImageAndReturnUpdatedUserResponse() throws Exception {
        // Arrange
        var imageDataUri = "imageDataUri";

        var request = new ProfileImageRequest(
                      imageDataUri
        );

        var requestAsJson = objectMapper.writeValueAsString(request);

        var updatedUser = new User();

        given(userService.changeProfileImage(MOCKED_UUID, imageDataUri)).willReturn(Optional.of(updatedUser));

        var expectedResponse = new UserResponse(
                MOCKED_UUID,
                "fullName",
                "username",
                "publicProfileImageId"
        );

        given(userResponseMapper.toUserResponse(updatedUser)).willReturn(expectedResponse);

        var expectedJson = objectMapper.writeValueAsString(expectedResponse);

        // Act
        // Assert
        mockMvc.perform(
                post("/api/v1/me/profile-image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestAsJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(userService).changeProfileImage(MOCKED_UUID, imageDataUri);
    }

    @Test
    @WithMockAppUser(userIdAsString = MOCKED_UUID_AS_STRING)
    void changeProfileImage_givenNotExistingUser_shouldReturnNotFound() throws Exception {
        // Arrange
        var imageDataUri = "imageDataUri";

        var request = new ProfileImageRequest(
                imageDataUri
        );

        var requestAsJson = objectMapper.writeValueAsString(request);

        given(userService.changeProfileImage(MOCKED_UUID, imageDataUri)).willReturn(Optional.empty());

        // Act
        // Assert
        mockMvc.perform(
                post("/api/v1/me/profile-image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestAsJson)
        )
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @CsvSource({
            ","
    })
    void changeProfileImage_givenInvalidRequestDto_shouldReturnErrorResponse(String imageDataUri) throws Exception {
        // Arrange
        var request = new ProfileImageRequest(
                imageDataUri
        );

        var requestAsJson = objectMapper.writeValueAsString(request);

        // Act
        // Assert
        var result = mockMvc.perform(
                post("/api/v1/me/profile-image")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson)
        )
                .andExpect(status().isBadRequest())
                .andReturn();

        var responseAsString = result.getResponse().getContentAsString();
        var response = objectMapper.readValue(responseAsString, ErrorResponse.class);

        assertThat(response.getErrors().size()).isGreaterThanOrEqualTo(1);
    }
}