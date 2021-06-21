package com.simonehleringer.instagramcloneapi.post.me;

import com.simonehleringer.instagramcloneapi.post.Post;
import com.simonehleringer.instagramcloneapi.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class FeedPostResponseMapperTest {
    private FeedPostResponseMapper underTest;

    @BeforeEach
    void setUp() {
        underTest = FeedPostResponseMapperUtils.generateFeedPostResponseMapper();
    }

    @Test
    void toFeedPostResponse_shouldMapProperly() {
        // Arrange
        var postId = 1;
        var publicImageId = "publicImageId";
        var text = "text";
        var creationTime = LocalDateTime.of(2000, 1, 1, 1, 1, 1);

        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var fullName = "fullName";
        var username = "username";
        var publicProfileImageId = "publicProfileImageId";

        var user = new User(
                userId,
                fullName,
                username,
                "email",
                "encodedPassword",
                "characteristics",
                publicProfileImageId,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        var post = new Post(
                postId,
                publicImageId,
                text,
                creationTime,
                user
        );

        // Act
        var feedPostResponse = underTest.toFeedPostResponse(post);

        // Assert
        assertThat(feedPostResponse.getPostId()).isEqualTo(postId);
        assertThat(feedPostResponse.getPublicImageId()).isEqualTo(publicImageId);
        assertThat(feedPostResponse.getText()).isEqualTo(text);
        assertThat(feedPostResponse.getCreationTime()).isEqualTo(creationTime);

        var creator = feedPostResponse.getCreator();

        assertThat(creator.getUserId()).isEqualTo(userId);
        assertThat(creator.getUsername()).isEqualTo(username);
        assertThat(creator.getFullName()).isEqualTo(fullName);
        assertThat(creator.getPublicProfileImageId()).isEqualTo(publicProfileImageId);
    }

    @Test
    void toFeedPostResponseList_shouldMapProperly() {
        // Arrange
        var postId = 1;
        var publicImageId = "publicImageId";
        var text = "text";
        var creationTime = LocalDateTime.of(2000, 1, 1, 1, 1, 1);

        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var fullName = "fullName";
        var username = "username";
        var publicProfileImageId = "publicProfileImageId";

        var user = new User(
                userId,
                fullName,
                username,
                "email",
                "encodedPassword",
                "characteristics",
                publicProfileImageId,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        var post = new Post(
                postId,
                publicImageId,
                text,
                creationTime,
                user
        );

        var postList = new ArrayList<Post>();
        postList.add(post);

        // Act
        var feedPostResponseList = underTest.toFeedPostResponseList(postList);

        // Assert
        assertThat(feedPostResponseList.size()).isEqualTo(1);

        var feedPostResponse = feedPostResponseList.get(0);

        assertThat(feedPostResponse.getPostId()).isEqualTo(postId);
        assertThat(feedPostResponse.getPublicImageId()).isEqualTo(publicImageId);
        assertThat(feedPostResponse.getText()).isEqualTo(text);
        assertThat(feedPostResponse.getCreationTime()).isEqualTo(creationTime);

        var creator = feedPostResponse.getCreator();

        assertThat(creator.getUserId()).isEqualTo(userId);
        assertThat(creator.getUsername()).isEqualTo(username);
        assertThat(creator.getFullName()).isEqualTo(fullName);
        assertThat(creator.getPublicProfileImageId()).isEqualTo(publicProfileImageId);
    }
}