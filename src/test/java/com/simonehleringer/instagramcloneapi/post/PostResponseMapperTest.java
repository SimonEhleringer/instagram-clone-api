package com.simonehleringer.instagramcloneapi.post;

import com.simonehleringer.instagramcloneapi.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostResponseMapperTest {
    private final PostResponseMapper underTest = Mappers.getMapper(PostResponseMapper.class);

    @Test
    void toPostResponse_shouldMapProperly() {
        // Arrange
        var postId = 1;
        var publicImageId = "publicImageId";
        var text = "text";
        var creationTime = LocalDateTime.of(2000, 1, 1, 1, 1, 1);
        var user = new User();

        var post = new Post(
                postId,
                publicImageId,
                text,
                creationTime,
                user
        );

        // Act
        var postResponse = underTest.toPostResponse(post);

        // Assert
        assertThat(postResponse.getPostId()).isEqualTo(postId);
        assertThat(postResponse.getPublicImageId()).isEqualTo(publicImageId);
        assertThat(postResponse.getText()).isEqualTo(text);
        assertThat(postResponse.getCreationTime()).isEqualTo(creationTime);
    }

    @Test
    void toPostResponseList_shouldMapProperly() {
        // Arrange
        var postId = 1;
        var publicImageId = "publicImageId";
        var text = "text";
        var creationTime = LocalDateTime.of(2000, 1, 1, 1, 1, 1);
        var user = new User();

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
        var postResponseList = underTest.toPostResponseList(postList);

        // Assert
        assertThat(postResponseList.size()).isEqualTo(1);

        var postResponse = postResponseList.get(0);

        assertThat(postResponse.getPostId()).isEqualTo(postId);
        assertThat(postResponse.getPublicImageId()).isEqualTo(publicImageId);
        assertThat(postResponse.getText()).isEqualTo(text);
        assertThat(postResponse.getCreationTime()).isEqualTo(creationTime);
    }
}