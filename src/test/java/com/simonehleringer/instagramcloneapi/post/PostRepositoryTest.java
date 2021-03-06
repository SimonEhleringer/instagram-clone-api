package com.simonehleringer.instagramcloneapi.post;

import com.simonehleringer.instagramcloneapi.user.User;
import com.simonehleringer.instagramcloneapi.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class PostRepositoryTest {
    @Autowired
    private PostRepository underTest;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void findByUserOrderByCreationTimeDesc_shouldReturnUsersPosts() {
        // Arrange
        var user = new User(
                null,
                "",
                "",
                "",
                "",
                "",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        var anotherUser = new User(
                null,
                "",
                "",
                "",
                "",
                "",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        userRepository.save(user);
        userRepository.save(anotherUser);

        var usersPost1 = new Post(
            0,
            "publicImageId",
            "text",
            LocalDateTime.of(2000, 1, 1, 1, 1),
            user
        );

        var usersPost2 = new Post(
                0,
                "publicImageId",
                "text",
                LocalDateTime.of(2001, 1, 1, 1, 1),
                user
        );

        var anotherPost = new Post(
            0,
            "anotherPublicImageId",
            "anotherText",
            LocalDateTime.of(2000, 1, 1, 1, 2),
            anotherUser
        );

        var createdUsersPost1 = underTest.save(usersPost1);
        var createdUsersPost2 = underTest.save(usersPost2);
        underTest.save(anotherPost);

        // Act
        var posts = underTest.findByUserOrderByCreationTimeDesc(user);

        // Assert
        assertThat(posts.size()).isEqualTo(2);
        assertThat(posts.get(0)).isEqualTo(createdUsersPost2);
        assertThat(posts.get(1)).isEqualTo(createdUsersPost1);
    }

    @Test
    void findByUserInOrderByCreationTimeDesc_shouldReturnPostsOfUsersInCorrectOrder() {
        // Arrange
        var user = new User(
                null,
                "",
                "",
                "",
                "",
                "",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        var anotherUser = new User(
                null,
                "",
                "",
                "",
                "",
                "",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        var createdUser = userRepository.save(user);
        var createdAnotherUser = userRepository.save(anotherUser);

        var usersPost1 = new Post(
            0,
            "",
            "",
            LocalDateTime.of(2000, 1, 1, 1, 1),
            createdUser
        );

        var usersPost2 = new Post(
            0,
            "",
            "",
            LocalDateTime.of(2001, 1, 1, 1, 1),
            createdUser
        );

        var createdUsersPost1 = underTest.save(usersPost1);
        var createdUsersPost2 = underTest.save(usersPost2);

        var anotherUsersPost = new Post(
            0,
            "",
            "",
            LocalDateTime.of(2000, 1, 1, 1, 1),
            createdAnotherUser
        );

        underTest.save(anotherUsersPost);

        var parameter = new ArrayList<User>();
        parameter.add(createdUser);

        // Act
        var posts = underTest.findByUserInOrderByCreationTimeDesc(parameter);

        // Assert
        assertThat(posts.size()).isEqualTo(2);

        assertThat(posts.get(0).getPostId()).isEqualTo(createdUsersPost2.getPostId());
        assertThat(posts.get(1).getPostId()).isEqualTo(createdUsersPost1.getPostId());
    }
}