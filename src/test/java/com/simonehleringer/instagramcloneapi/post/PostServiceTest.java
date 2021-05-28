package com.simonehleringer.instagramcloneapi.post;

import com.simonehleringer.instagramcloneapi.cloudinary.CloudinaryService;
import com.simonehleringer.instagramcloneapi.cloudinary.ImageType;
import com.simonehleringer.instagramcloneapi.common.ValidationService;
import com.simonehleringer.instagramcloneapi.user.User;
import com.simonehleringer.instagramcloneapi.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolationException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    private final LocalDateTime MOCKED_LOCAL_DATE_TIME = LocalDateTime.of(2000, 1, 1, 1, 1, 1);

    @InjectMocks
    private PostService underTest;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private ValidationService validationService;

    @Mock
    private UserService userService;

    @Mock
    private Clock clock;

    private Clock fixedClock;

    @Test
    void createPost_givenValidParameters_shouldCreatePost() {
        // Arrange
        fixedClock = Clock.fixed(
                MOCKED_LOCAL_DATE_TIME.toInstant(ZoneOffset.UTC),
                ZoneId.of("UTC")
        );

        given(clock.instant()).willReturn(fixedClock.instant());
        given(clock.getZone()).willReturn(fixedClock.getZone());

        var text = "text";
        var imageDataUri = "imageDataUri";
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var publicImageId = "publicImageId";
        var postId = 1;

        var user = new User(
                userId,
                "",
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

        var expectedCreatedPost = new Post(
                postId,
                publicImageId,
                text,
                MOCKED_LOCAL_DATE_TIME,
                user
        );

        given(userService.getById(userId)).willReturn(Optional.of(user));
        given(postRepository.save(any())).willReturn(expectedCreatedPost);
        given(cloudinaryService.uploadImage(imageDataUri, ImageType.POST, userId)).willReturn(publicImageId);

        // Act
        var optionalCreatedPost = underTest.createPost(text, imageDataUri, userId);

        // Assert
        var createdPost = optionalCreatedPost.get();

        assertThat(createdPost).isSameAs(expectedCreatedPost);

        var postCaptor = ArgumentCaptor.forClass(Post.class);

        verify(postRepository).save(postCaptor.capture());

        var capturedPost = postCaptor.getValue();

        assertThat(capturedPost.getPublicImageId()).isEqualTo(publicImageId);
        assertThat(capturedPost.getText()).isEqualTo(text);
        assertThat(capturedPost.getCreationTime()).isEqualTo(MOCKED_LOCAL_DATE_TIME);
        assertThat(capturedPost.getUser()).isSameAs(user);

        verify(cloudinaryService).uploadImage(imageDataUri, ImageType.POST, userId);
    }

    @Test
    void createPost_givenNotExistingUserId_shouldReturnEmptyOptional() {
        // Arrange
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        given(userService.getById(userId)).willReturn(Optional.empty());

        // Act
        var optionalCreatedPost = underTest.createPost("text", "imageDataUri", userId);

        // Assert
        assertThat(optionalCreatedPost).isEmpty();

        verify(cloudinaryService, never()).uploadImage(anyString(), any(), any());
        verify(postRepository, never()).save(any());
    }

    @Test
    void createPost_givenInvalidParameters_shouldThrowAndNotUploadImageToCloudinary() {
        // Arrange
        fixedClock = Clock.fixed(
                MOCKED_LOCAL_DATE_TIME.toInstant(ZoneOffset.UTC),
                ZoneId.of("UTC")
        );

        given(clock.instant()).willReturn(fixedClock.instant());
        given(clock.getZone()).willReturn(fixedClock.getZone());

        var text = "text";
        var imageDataUri = "imageDataUri";
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        var user = new User(
                userId,
                "",
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

        given(userService.getById(userId)).willReturn(Optional.of(user));
        doThrow(ConstraintViolationException.class).when(validationService).validate(any());

        // Act
        // Assert
        assertThatThrownBy(() ->
                underTest.createPost(text, imageDataUri, userId));

        verify(cloudinaryService, never()).uploadImage(anyString(), any(), any());
        verify(postRepository, never()).save(any());
    }

    @Test
    void createPost_shouldValidatePostToCreateBeforeUploadingImageToCloudinary() {
        // Arrange
        fixedClock = Clock.fixed(
                MOCKED_LOCAL_DATE_TIME.toInstant(ZoneOffset.UTC),
                ZoneId.of("UTC")
        );

        given(clock.instant()).willReturn(fixedClock.instant());
        given(clock.getZone()).willReturn(fixedClock.getZone());

        var text = "text";
        var imageDataUri = "imageDataUri";
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        var user = new User(
                userId,
                "",
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

        given(userService.getById(userId)).willReturn(Optional.of(user));
        given(postRepository.save(any())).willReturn(new Post());

        // Act
        underTest.createPost(text, imageDataUri, userId);

        // Assert
        var createdPostCaptor = ArgumentCaptor.forClass(Post.class);

        verify(validationService).validate(createdPostCaptor.capture());

        var capturedCreatedPost = createdPostCaptor.getValue();

        assertThat(capturedCreatedPost.getText()).isEqualTo(text);
        assertThat(capturedCreatedPost.getUser()).isEqualTo(user);
        assertThat(capturedCreatedPost.getCreationTime()).isEqualTo(MOCKED_LOCAL_DATE_TIME);
        assertThat(capturedCreatedPost.getPublicImageId()).isNull();
    }
}