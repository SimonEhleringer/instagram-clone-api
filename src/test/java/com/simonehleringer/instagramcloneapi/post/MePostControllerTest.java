package com.simonehleringer.instagramcloneapi.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonehleringer.instagramcloneapi.common.ErrorResponse;
import com.simonehleringer.instagramcloneapi.common.jwtAuthentication.JwtAuthenticationEntryPoint;
import com.simonehleringer.instagramcloneapi.testUtil.annotation.WithMockAppUser;
import com.simonehleringer.instagramcloneapi.user.MeController;
import com.simonehleringer.instagramcloneapi.user.User;
import com.simonehleringer.instagramcloneapi.user.UserService;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MePostController.class)
@AutoConfigureMockMvc(addFilters = false)
class MePostControllerTest {
    private final String MOCKED_UUID_AS_STRING = "11111111-1111-1111-1111-111111111111";
    private final UUID MOCKED_UUID = UUID.fromString(MOCKED_UUID_AS_STRING);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostResponseMapper postResponseMapper;

    @MockBean
    private PostsResponseMapper postsResponseMapper;

    @Test
    @WithMockAppUser(userIdAsString = MOCKED_UUID_AS_STRING)
    void add_givenValidRequestDto_shouldReturnCreatedPost() throws Exception {
        // Arrange
        var imageDataUri = "imageDataUri";
        var text = "text";

        var postId = 1;
        var publicImageId = "publicImageId";
        var creationTime = LocalDateTime.of(2000, 1, 1, 1, 1);

        var request = new PostRequest(
                imageDataUri,
                text
        );

        var createdPost = new Post();

        given(postService.createPost(text, imageDataUri, MOCKED_UUID)).willReturn(Optional.of(createdPost));

        var expectedResponse = new PostResponse(
            postId,
            publicImageId,
            text,
            creationTime
        );

        given(postResponseMapper.toPostResponse(createdPost)).willReturn(expectedResponse);

        var expectedJson = objectMapper.writeValueAsString(expectedResponse);

        var requestAsJson = objectMapper.writeValueAsString(request);

        // Act
        // Assert
        mockMvc.perform(
                post("/api/v1/me/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestAsJson)
        )
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson))
                .andExpect(header().stringValues(HttpHeaders.LOCATION, "http://localhost/api/v1/me/posts/" + postId));

        verify(postService).createPost(text, imageDataUri, MOCKED_UUID);
    }

    @ParameterizedTest
    @CsvSource({
            ", text",
            "imageDataUri, AaaaaaaaaaAaaaaaaaaaAaaaaaaaaaAaaaaaaaaaAaaaaaaaaaAaaaaaaaaaAaaaaaaaaaAaaaaaaaaaAaaaaaaaaaAaaaaaaaaaAaaaaaaaaaAaaaaaaaaaAaaaaaaaaaAaaaaaaaaaAaaaaaaaaaA"
    })
    @WithMockAppUser
    void add_givenInvalidRequestDto_shouldReturnErrorResponse(String imageDataUri, String text) throws Exception {
        // Arrange
        var request = new PostRequest(
                imageDataUri,
                text
        );

        var requestAsJson = objectMapper.writeValueAsString(request);

        // Act
        // Assert
        var result = mockMvc.perform(
                post("/api/v1/me/posts")
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

    @Test
    @WithMockAppUser(userIdAsString = MOCKED_UUID_AS_STRING)
    void getAll_shouldReturnPosts() throws Exception {
        // Arrange
        var posts = new ArrayList<Post>();

        var postResponse = new PostResponse(
            1,
            "publicImageId",
            "text",
            LocalDateTime.of(2000, 1, 1, 1, 1)
        );

        var postResponses = new ArrayList<PostResponse>();
        postResponses.add(postResponse);

        var expectedResponse = new PostsResponse(postResponses);

        var expectedJson = objectMapper.writeValueAsString(expectedResponse);

        given(postService.getAllUsersPosts(MOCKED_UUID)).willReturn(Optional.of(posts));
        given(postsResponseMapper.toPostResponses(posts)).willReturn(expectedResponse);

        // Act
        // Assert
        mockMvc.perform(
                get("/api/v1/me/posts")
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }
}