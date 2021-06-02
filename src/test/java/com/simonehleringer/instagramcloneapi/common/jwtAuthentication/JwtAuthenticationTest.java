package com.simonehleringer.instagramcloneapi.common.jwtAuthentication;

import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.accessToken.AccessTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthenticationTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AccessTokenService accessTokenService;

    @Test
    void jwtAuthentication_givenValidJwt_shouldNotReturnUnauthorized() throws Exception {
        // Arrange
        var accessToken = "jwtToken";
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        var header = new DefaultJwsHeader(new HashMap<>());
        var body = new DefaultClaims(new HashMap<>());
        body.setSubject(userId.toString());

        var claimsJws = new DefaultJws<Claims>(header, body, "signature");

        given(accessTokenService.parseAccessToken(accessToken)).willReturn(claimsJws);

        // Act
        // Assert
        mockMvc.perform(
                get("/api/v1/someUrlThatDoesNotExist")
                        .header(HttpHeaders.AUTHORIZATION, "bearer " + accessToken)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    void jwtAuthentication_givenInvalidJwt_shouldReturnUnauthorized() throws Exception {
        // Arrange
        given(accessTokenService.parseAccessToken(anyString())).willThrow(JwtException.class);

        // Act
        // Assert
        mockMvc.perform(
                get("/api/v1/someUrlThatDoesNotExist")
                        .header(HttpHeaders.AUTHORIZATION, "bearer jwtToken")
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void jwtAuthentication_givenNoBearerToken_shouldReturnUnauthorized() throws Exception {
        // Act
        // Assert
        mockMvc.perform(
                get("/api/v1/someUrlThatDoesNotExist")
                        .header(HttpHeaders.AUTHORIZATION, "jwtToken")
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void jwtAuthentication_givenNoAuthorizationHeader_shouldReturnUnauthorized() throws Exception {
        // Act
        // Assert
        mockMvc.perform(
                get("/api/v1/someUrlThatDoesNotExist")
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void jwtAuthentication_givenRequestToAuthenticationApiAndNoAuthorizationHeader_shouldNotReturnUnauthorized() throws Exception {
        // Act
        // Assert
        mockMvc.perform(
                get("/api/v1/authentication/someUrlThatDoesNotExist")
        )
                .andExpect(status().isNotFound());
    }
}
