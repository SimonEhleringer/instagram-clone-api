package com.simonehleringer.instagramcloneapi.post.me;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonehleringer.instagramcloneapi.common.jwtAuthentication.JwtAuthenticationEntryPoint;
import com.simonehleringer.instagramcloneapi.post.Post;
import com.simonehleringer.instagramcloneapi.post.PostResponse;
import com.simonehleringer.instagramcloneapi.post.PostResponseMapper;
import com.simonehleringer.instagramcloneapi.post.PostService;
import com.simonehleringer.instagramcloneapi.post.me.FeedResponse;
import com.simonehleringer.instagramcloneapi.post.me.FeedResponseMapper;
import com.simonehleringer.instagramcloneapi.post.me.MeFeedController;
import com.simonehleringer.instagramcloneapi.testUtil.annotation.WithMockAppUser;
import com.simonehleringer.instagramcloneapi.user.UserResponse;
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

@SuppressWarnings("DefaultAnnotationParam")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MeFeedController.class)
@AutoConfigureMockMvc(addFilters = false)
class MeFeedControllerTest {
    private final String loggedInUserIdAsString = "11111111-1111-1111-1111-111111111111";
    private final UUID loggedInUserId = UUID.fromString(loggedInUserIdAsString);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private FeedResponseMapper feedResponseMapper;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private PostResponseMapper postResponseMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockAppUser(userIdAsString = loggedInUserIdAsString)
    void get_shouldReturnLoggedInUsersFeed() throws Exception {
        // Arrange
        var postsList = new ArrayList<Post>();
        var postsResponseList = new ArrayList<FeedPostResponse>();

        postsResponseList.add(new FeedPostResponse(
                1,
                "publicImageId",
                "text",
                LocalDateTime.of(2000, 1, 1, 1, 1),
                new UserResponse(
                        UUID.fromString("22222222-2222-2222-2222-222222222222"),
                        "fullName",
                        "username",
                        "publicProfileImageId"
                )
        ));

        var expectedResponse = new FeedResponse(postsResponseList);
        var expectedJson = objectMapper.writeValueAsString(expectedResponse);

        given(postService.getUsersFeed(loggedInUserId)).willReturn(Optional.of(postsList));
        given(feedResponseMapper.toFeedResponse(postsList)).willReturn(expectedResponse);

        // Act
        // Assert
        mockMvc.perform(
                get("/api/v1/me/feed")
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }
}