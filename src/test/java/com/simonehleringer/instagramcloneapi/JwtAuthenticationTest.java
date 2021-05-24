package com.simonehleringer.instagramcloneapi;

import com.simonehleringer.instagramcloneapi.authentication.AccessAndRefreshTokenResponseMapper;
import com.simonehleringer.instagramcloneapi.authentication.AuthenticationService;
import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.accessToken.AccessTokenService;
import com.simonehleringer.instagramcloneapi.common.jwtAuthentication.JwtAuthenticationEntryPoint;
import com.simonehleringer.instagramcloneapi.common.jwtAuthentication.UserPrincipal;
import com.simonehleringer.instagramcloneapi.user.MeController;
import com.simonehleringer.instagramcloneapi.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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
}