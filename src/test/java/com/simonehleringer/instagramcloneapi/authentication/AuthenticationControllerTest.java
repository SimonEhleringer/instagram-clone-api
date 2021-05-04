package com.simonehleringer.instagramcloneapi.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonehleringer.instagramcloneapi.ErrorResponse;
import com.simonehleringer.instagramcloneapi.SecurityConfig;
import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.AccessAndRefreshToken;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private AccessAndRefreshTokenResponseMapper accessAndRefreshTokenResponseMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_givenValidBody_shouldReturnAccessAndRefreshToken() throws Exception {
        // Arrange
        var usernameOrEmail = "UsernameOrEmail";
        var password = "Password!";

        var request = new LoginRequest(
                usernameOrEmail,
                password
        );

        var requestAsJson = objectMapper.writeValueAsString(request);

        var accessToken = "accessToken";
        var refreshToken = "refreshToken";

        var accessAndRefreshToken = new AccessAndRefreshToken(
                accessToken,
                refreshToken
        );

        var accessAndRefreshTokenResponse = new AccessAndRefreshTokenResponse(
                accessToken,
                refreshToken
        );

        var expectedJson = objectMapper.writeValueAsString(accessAndRefreshTokenResponse);

        when(authenticationService.login(
                usernameOrEmail,
                password
        )).thenReturn(accessAndRefreshToken);

        given(accessAndRefreshTokenResponseMapper.toAccessAndRefreshTokenResponse(accessAndRefreshToken))
                .willReturn(accessAndRefreshTokenResponse);

        // Act
        // Assert
        mockMvc.perform(
                post("/api/v1/authentication/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(authenticationService).login(usernameOrEmail, password);
        verify(accessAndRefreshTokenResponseMapper).toAccessAndRefreshTokenResponse(accessAndRefreshToken);
    }

    @ParameterizedTest
    @CsvSource({
            "'' , Password",
            ", Password",
            "usernameOrEmail, ''",
            "usernameOrEmail, "
    })
    void login_givenInvalidBody_shouldReturnErrorModel(String usernameOrEmail, String password) throws Exception {
        // Arrange
        var request = new LoginRequest(usernameOrEmail, password);
        var requestAsJson = objectMapper.writeValueAsString(request);

        // Act
        // Assert
        var result = mockMvc.perform(
                post("/api/v1/authentication/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson)
        )
                .andExpect(status().isBadRequest())
                .andReturn();

        var bodyAsString = result.getResponse().getContentAsString();
        var body = objectMapper.readValue(bodyAsString, ErrorResponse.class);

        assertThat(body.getErrors().size()).isGreaterThan(0);

        verify(authenticationService, never()).login(anyString(), anyString());
        verify(accessAndRefreshTokenResponseMapper, never()).toAccessAndRefreshTokenResponse(any());
    }

    @Test
    void register_givenValidBody_shouldReturnAccessAndRefreshToken() throws Exception {
        // Arrange
        var email = "Test@Test.com";
        var fullName = "Test Test";
        var username = "Test";
        var password = "Test123!";

        var request = new RegisterRequest(
                email,
                fullName,
                username,
                password
        );

        var requestAsString = objectMapper.writeValueAsString(request);

        var accessToken = "accessToken";
        var refreshToken = "refreshToken";

        var accessAndRefreshToken = new AccessAndRefreshToken(
                accessToken,
                refreshToken
        );

        var accessAndRefreshTokenResponse = new AccessAndRefreshTokenResponse(
                accessToken,
                refreshToken
        );

        var expectedJson = objectMapper.writeValueAsString(accessAndRefreshTokenResponse);

        given(authenticationService.register(
                fullName,
                username,
                email,
                password
        )).willReturn(accessAndRefreshToken);

        given(accessAndRefreshTokenResponseMapper.toAccessAndRefreshTokenResponse(accessAndRefreshToken))
                .willReturn(accessAndRefreshTokenResponse);

        // Act
        // Assert
        mockMvc.perform(
                post("/api/v1/authentication/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsString)
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(authenticationService).register(
                fullName,
                username,
                email,
                password
        );

        verify(accessAndRefreshTokenResponseMapper).toAccessAndRefreshTokenResponse(accessAndRefreshToken);
    }

    @ParameterizedTest
    @CsvSource({
            "'', Test Test, Test, Test123!",
            ", Test Test, Test, Test123!",
            "SomeEmail, Test Test, Test, Test123!",
            "Test@Test.com, , Username, Test123!",
            "Test@Test.com, 'AaaaaaaaaaaAaaaaaaaaaAaaaaaaaaaA', Username, Test123!",
            "Test@Test.com, FullName, '', Test123!",
            "Test@Test.com, FullName, , Test123!",
            "Test@Test.com, FullName, AaaaaaaaaaaAaaaaaaaaaAaaaaaaaaaA, Test123!",
            "Test@Test.com, FullName, *, Test123!",
            "Test@Test.com, FullName, Username, ''",
            "Test@Test.com, FullName, Username, ",
            "Test@Test.com, FullName, Username, Te1!",
            "Test@Test.com, FullName, Username, AaaaaaaaaaAaaaaaaaaaAaaaaaaaaaAaaaaaaaaaAaaaaaaaa1!",
            "Test@Test.com, FullName, Username, Test123",
            "Test@Test.com, FullName, Username, Test!!!",
            "Test@Test.com, FullName, Username, TEST123!",
            "Test@Test.com, FullName, Username, test123!"
    })
    void register_givenInvalidBody_shouldReturnErrorResponse(String email, String fullName, String username, String password) throws Exception {
        // Arrange
        var request = new RegisterRequest(
                email,
                fullName,
                username,
                password
        );

        var requestAsString = objectMapper.writeValueAsString(request);

        // Act
        // Assert
        var result = mockMvc.perform(
                post("/api/v1/authentication/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsString)
        )
                .andExpect(status().isBadRequest())
                .andReturn();

        var responseAsString = result.getResponse().getContentAsString();
        var response = objectMapper.readValue(responseAsString, ErrorResponse.class);

        assertThat(response.getErrors().size()).isGreaterThan(0);

        verify(authenticationService, never()).register(anyString(), anyString(), anyString(), anyString());
        verify(accessAndRefreshTokenResponseMapper, never()).toAccessAndRefreshTokenResponse(any());
    }

    @Test
    void refreshAccessToken_givenValidBody_shouldReturnNewAccessAndRefreshToken() throws Exception {
        // Arrange
        var refreshToken = "refreshToken";

        var request = new RefreshTokenRequest(
                refreshToken
        );

        var requestAsString = objectMapper.writeValueAsString(request);

        var newAccessToken = "newAccessToken";
        var newRefreshToken = "newRefreshToken";

        var accessAndRefreshToken = new AccessAndRefreshToken(
                newAccessToken,
                newRefreshToken
        );

        var accessAndRefreshTokenResponse = new AccessAndRefreshTokenResponse(
                newAccessToken,
                newRefreshToken
        );

        var expectedJson = objectMapper.writeValueAsString(accessAndRefreshTokenResponse);

        given(authenticationService.refreshAccessToken(refreshToken)).willReturn(accessAndRefreshToken);
        given(accessAndRefreshTokenResponseMapper.toAccessAndRefreshTokenResponse(accessAndRefreshToken))
                .willReturn(accessAndRefreshTokenResponse);

        // Act
        // Assert
        mockMvc.perform(
                post("/api/v1/authentication/refreshAccessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestAsString)
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(authenticationService).refreshAccessToken(refreshToken);
        verify(accessAndRefreshTokenResponseMapper).toAccessAndRefreshTokenResponse(accessAndRefreshToken);
    }

    @ParameterizedTest
    @CsvSource({
            "''",
            ","
    })
    void refreshAccessAndRefreshToken_givenInvalidBody_shouldReturnErrorResponse(String refreshToken) throws Exception {
        // Arrange
        var request = new RefreshTokenRequest(refreshToken);
        var requestAsString = objectMapper.writeValueAsString(request);

        // Act
        // Assert
        var result = mockMvc.perform(
                post("/api/v1/authentication/refreshAccessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestAsString)
        )
                .andExpect(status().isBadRequest())
                .andReturn();

        var responseAsString = result.getResponse().getContentAsString();
        var response = objectMapper.readValue(responseAsString, ErrorResponse.class);

        assertThat(response.getErrors().size()).isGreaterThan(0);
        verify(authenticationService, never()).refreshAccessToken(anyString());
        verify(accessAndRefreshTokenResponseMapper, never()).toAccessAndRefreshTokenResponse(any());
    }

    @Test
    void logout_givenValidBody_shouldReturnNoContent() throws Exception {
        // Arrange
        var refreshToken = "refreshToken";

        var request = new RefreshTokenRequest(
                refreshToken
        );

        var requestAsString = objectMapper.writeValueAsString(request);

        // Act
        // Assert
        mockMvc.perform(
                post("/api/v1/authentication/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestAsString)
        )
                .andExpect(status().isNoContent());

        verify(authenticationService).logout(refreshToken);
    }

    @ParameterizedTest
    @CsvSource({
            "''",
            ","
    })
    void logout_givenInvalidBody_shouldReturnErrorResponse(String refreshToken) throws Exception {
        // Arrange
        var request = new RefreshTokenRequest(refreshToken);
        var requestAsString = objectMapper.writeValueAsString(request);

        // Act
        // Assert
        var result = mockMvc.perform(
                post("/api/v1/authentication/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestAsString)
        )
                .andExpect(status().isBadRequest())
                .andReturn();

        var responseAsString = result.getResponse().getContentAsString();
        var response = objectMapper.readValue(responseAsString, ErrorResponse.class);

        assertThat(response.getErrors().size()).isGreaterThan(0);
        verify(authenticationService, never()).logout(anyString());
    }
}