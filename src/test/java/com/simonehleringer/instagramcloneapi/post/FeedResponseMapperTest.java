package com.simonehleringer.instagramcloneapi.post;

import com.simonehleringer.instagramcloneapi.user.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FeedResponseMapperTest {
    private final FeedResponseMapper underTest = Mappers.getMapper(FeedResponseMapper.class);

    @Test
    void toFeedResponse_shouldMapProperly() {
        // Arrange
        var postId = 1;
        var publicImageId = "publicImageId";
        var text = "text";
        var creationTime = LocalDateTime.of(2000, 1, 1, 1, 1);

        var post = new Post(
                postId,
                publicImageId,
                text,
                creationTime,
                new User()
        );

        var posts = new ArrayList<Post>();
        posts.add(post);

        // Act
        var feedResponse = underTest.toFeedResponse(posts);

        // Assert
        var feed = feedResponse.getFeed();

        assertThat(feed.size()).isEqualTo(1);

        var postResponse = feed.get(0);

        assertThat(postResponse.getPostId()).isEqualTo(postId);
        assertThat(postResponse.getPublicImageId()).isEqualTo(publicImageId);
        assertThat(postResponse.getText()).isEqualTo(text);
        assertThat(postResponse.getCreationTime()).isEqualTo(creationTime);
    }
}