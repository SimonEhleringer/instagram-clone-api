package com.simonehleringer.instagramcloneapi.post;

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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserPostController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserPostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostsResponseMapper postsResponseMapper;

    @Test
    @WithMockAppUser
    void getAll_givenExistingUser_shouldReturnPosts() throws Exception {
        // Arrange
        var userId = UUID.fromString("22222222-2222-2222-2222-222222222222");

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

        given(postService.getAllUsersPosts(userId)).willReturn(Optional.of(posts));
        given(postsResponseMapper.toPostResponses(posts)).willReturn(expectedResponse);

        // Act
        // Assert
        mockMvc.perform(
                get("/api/v1/users/" + userId.toString() + "/posts")
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockAppUser
    void getAll_givenNotExistingUser_shouldReturnNotFound() throws Exception {
        // Arrange
        var userId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        given(postService.getAllUsersPosts(userId)).willReturn(Optional.empty());

        // Act
        // Assert
        mockMvc.perform(
                get("/api/v1/users/" + userId.toString() + "/posts")
        )
                .andExpect(status().isNotFound());
    }
}