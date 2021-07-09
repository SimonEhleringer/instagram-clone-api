package com.simonehleringer.instagramcloneapi.user;

import com.simonehleringer.instagramcloneapi.cloudinary.CloudinaryService;
import com.simonehleringer.instagramcloneapi.cloudinary.ImageType;
import com.simonehleringer.instagramcloneapi.common.ValidationService;
import com.simonehleringer.instagramcloneapi.user.me.CanNotAddFollowException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ValidationService validationService;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private UserService underTest;

    @Test
    void createUser_givenNotExistingUser_shouldSaveUser() {
        // Arrange
        var user = new User(
                "Firstname Lastname",
                "Username",
                "Firstname.Lastname@mail.com"
        );

        var password = "password";

        given(userRepository.existsByUsernameIgnoreCase(anyString())).willReturn(false);

        given(userRepository.existsByEmailIgnoreCase(anyString())).willReturn(false);

        // Act
        underTest.createUser(user, password);

        // Assert
        verify(validationService).validate(user);
        verify(validationService).validate(password);

        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userArgumentCaptor.capture());

        var capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(user);

        verify(passwordEncoder).encode(password);
    }

    @Test
    void createUser_givenExistingUsername_shouldThrowCanNotCreateUserException() {
        // Arrange
        var user = new User(
                "Firstname Lastname",
                "Username",
                "Firstname.Lastname@mail.com"
        );

        var password = "password";

        given(userRepository.existsByUsernameIgnoreCase(anyString())).willReturn(true);

        // Act
        // Assert
        assertThatThrownBy(() ->
                underTest.createUser(user, password))
                .isInstanceOf(CanNotCreateUserException.class);

        verify(validationService).validate(user);
        verify(validationService).validate(password);

        verify(userRepository, never()).existsByEmailIgnoreCase(anyString());

        verify(passwordEncoder, never()).encode(anyString());

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_givenExistingEmail_shouldThrowCanNotCreateUserException() {
        // Arrange
        var username = "Username";

        var user = new User(
                "Firstname Lastname",
                username,
                "Firstname.Lastname@mail.com"
        );

        var password = "password";

        given(userRepository.existsByEmailIgnoreCase(anyString())).willReturn(true);

        // Act
        // Assert
        assertThatThrownBy(() ->
                underTest.createUser(user, password))
                .isInstanceOf(CanNotCreateUserException.class);

        verify(validationService).validate(user);
        verify(validationService).validate(password);

        verify(userRepository).existsByUsernameIgnoreCase(username);

        verify(passwordEncoder, never()).encode(anyString());

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_givenNullUserToCreate_shouldThrow() {
        assertThatThrownBy(() ->
                underTest.createUser(null, "password"))
                .isInstanceOf(NullPointerException.class);

        verify(validationService, never()).validate(any());

        verify(passwordEncoder, never()).encode(anyString());

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_givenNullPassword_shouldThrow() {
        assertThatThrownBy(() ->
                underTest.createUser(new User(), null))
                .isInstanceOf(NullPointerException.class);

        verify(validationService, never()).validate(any());

        verify(passwordEncoder, never()).encode(anyString());

        verify(userRepository, never()).save(any());
    }

    @Test
    void getByUsername_shouldReturnUser() {
        // Arrange
        var username = "Username";

        var expected = Optional.of(new User(
                "FullName",
                username,
                "FullName@mail.com",
                "EncodedPassword",
                "ProfileImageLocation"
        ));

        given(userRepository.findByUsernameIgnoreCase(username)).willReturn(expected);

        // Act
        var actual = underTest.getByUsername(username);

        // Assert
        verify(userRepository).findByUsernameIgnoreCase(username);
        assertThat(actual).isSameAs(expected);
    }

    @Test
    void getByEmail_shouldReturnUser() {
        // Arrange
        var email = "FullName@gmail.com";

        var expected = Optional.of(new User(
                "FullName",
                "Username",
                email,
                "EncodedPassword",
                "ProfileImageLocation"
        ));

        given(userRepository.findByEmailIgnoreCase(email)).willReturn(expected);

        // Act
        var actual = underTest.getByEmail(email);

        // Assert
        verify(userRepository).findByEmailIgnoreCase(email);
        assertThat(actual).isSameAs(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "true",
            "false"
    })
    void checkPassword_checksPasswordAndReturnsResult(boolean expected) {
        // Arrange
        var user = new User(
                "",
                "",
                "",
                "EncodedPassword",
                ""
        );

        var password = "Password";

        given(passwordEncoder.matches(password, user.getEncodedPassword())).willReturn(expected);

        // Act
        var actual = underTest.checkPassword(user, password);

        // Assert
        verify(passwordEncoder).matches(password, user.getEncodedPassword());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getById_shouldReturnUser() {
        // Arrange
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        var expected = Optional.of(new User());

        given(userRepository.findById(userId)).willReturn(expected);

        // Act
        var actual = underTest.getById(userId);

        // Assert
        verify(userRepository).findById(userId);
        assertThat(actual).isSameAs(expected);
    }

    @Test
    void addFollow_givenExistingFollowerIdAndFollowedId_shouldReturnTrue() {
        // Arrange
        var followedId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var followerId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        var followed = new User();
        var followedFollowers = new ArrayList<User>();
        followed.setFollowers(followedFollowers);

        var follower = new User();
        var followersFollowed = new ArrayList<User>();
        follower.setFollowed(followersFollowed);

        given(userRepository.findById(followedId)).willReturn(Optional.of(followed));
        given(userRepository.findById(followerId)).willReturn(Optional.of(follower));

        // Act
        var hasBeenAdded = underTest.addFollow(followerId, followedId);

        // Assert
        assertThat(hasBeenAdded).isTrue();
    }

    @Test
    void addFollow_givenExistingFollowerIdButNonExistingFollowedId_shouldReturnFalse() {
        // Arrange
        var followedId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var followerId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        var follower = new User();
        follower.setFollowed(new ArrayList<>());

        given(userRepository.findById(followerId)).willReturn(Optional.of(follower));
        given(userRepository.findById(followedId)).willReturn(Optional.empty());

        // Act
        var hasBeenAdded = underTest.addFollow(followerId, followedId);

        // Assert
        assertThat(hasBeenAdded).isFalse();
    }

    @Test
    void addFollow_givenNonExistingFollowerId_shouldReturnFalse() {
        // Arrange
        var followedId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var followerId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        given(userRepository.findById(followerId)).willReturn(Optional.empty());

        // Act
        var hasBeenAdded = underTest.addFollow(followerId, followedId);

        // Assert
        assertThat(hasBeenAdded).isFalse();
    }

    @Test
    void addFollow_givenEqualFollowerIdAndFollowedId_shouldThrow() {
        // Arrange
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        // Act
        // Assert
        assertThatThrownBy(() ->
                underTest.addFollow(userId, userId))
                .isInstanceOf(CanNotAddFollowException.class);
    }

    @Test
    void addFollow_givenAlreadyFollowedUser_shouldThrow() {
        // Arrange
        var followedId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var followerId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        var followed = new User();
        followed.setUserId(followedId);

        var followedList = new ArrayList<User>();
        followedList.add(followed);

        var follower = new User();
        follower.setUserId(followerId);
        follower.setFollowed(followedList);

        given(userRepository.findById(followerId)).willReturn(Optional.of(follower));

        // Act
        // Assert
        assertThatThrownBy(() ->
                underTest.addFollow(followerId, followedId))
                .isInstanceOf(CanNotAddFollowException.class);
    }

    @Test
    void getUsersFollowed_givenExistingUser_shouldReturnUsersFollowed() {
        // Arrange
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        var followed = new User();
        var followedList = new ArrayList<User>();
        followedList.add(followed);

        var user = new User(
            userId,
            "",
            "",
            "",
            "",
            "",
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            followedList
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // Act
        var optionalFollowedList = underTest.getUsersFollowed(userId);

        // Assert
        var actualFollowedList = optionalFollowedList.get();

        assertThat(actualFollowedList.size()).isEqualTo(1);
        assertThat(actualFollowedList.get(0)).isSameAs(followed);
    }

    @Test
    void getUsersFollowed_givenNotExistingUser_shouldReturnEmptyOptional() {
        // Arrange
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        given(userRepository.findById(userId)).willReturn(Optional.empty());
        // Act
        var optionalFollowedList = underTest.getUsersFollowed(userId);

        // Assert
        assertThat(optionalFollowedList).isEmpty();
    }

    @Test
    void getUsersFollowers_givenExistingUser_shouldReturnUsersFollowers() {
        // Arrange
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        var followers = new User();
        var followersList = new ArrayList<User>();
        followersList.add(followers);

        var user = new User(
                userId,
                "",
                "",
                "",
                "",
                "",
                new ArrayList<>(),
                new ArrayList<>(),
                followersList,
                new ArrayList<>()
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // Act
        var optionalFollowersList = underTest.getUsersFollowers(userId);

        // Assert
        var actualFollowersList = optionalFollowersList.get();

        assertThat(actualFollowersList.size()).isEqualTo(1);
        assertThat(actualFollowersList.get(0)).isSameAs(followers);
    }

    @Test
    void getUsersFollowers_givenNotExistingUser_shouldReturnEmptyOptional() {
        // Arrange
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        given(userRepository.findById(userId)).willReturn(Optional.empty());
        // Act
        var optionalFollowersList = underTest.getUsersFollowers(userId);

        // Assert
        assertThat(optionalFollowersList).isEmpty();
    }

    @Test
    void removeFollow_givenExistingFollowerAndFollowed_shouldRemoveFollow() {
        // Arrange
        var followerId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var followedId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        var follower = new User(
                followerId,
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

        var followed = new User(
                followedId,
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

        follower.getFollowed().add(followed);
        followed.getFollowers().add(follower);

        given(userRepository.findById(followerId)).willReturn(Optional.of(follower));
        given(userRepository.findById(followedId)).willReturn(Optional.of(followed));

        // Act
        var hasBeenRemoved = underTest.removeFollow(followerId, followedId);

        // Assert
        assertThat(hasBeenRemoved).isTrue();
    }

    @Test
    void removeFollow_givenFollowedThatWasNotFollowedByFollower_shouldReturnFalse() {
        // Arrange
        var followerId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var followedId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        var follower = new User(
                followerId,
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

        var followed = new User(
                followedId,
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

        given(userRepository.findById(followerId)).willReturn(Optional.of(follower));
        given(userRepository.findById(followedId)).willReturn(Optional.of(followed));

        // Act
        var hasBeenRemoved = underTest.removeFollow(followerId, followedId);

        // Assert
        assertThat(hasBeenRemoved).isFalse();
    }

    @Test
    void removeFollow_givenExistingFollowerButNotExistingFollowed_shouldReturnFalse() {
        // Arrange
        var followerId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var followedId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        var follower = new User(
                followerId,
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

        given(userRepository.findById(followerId)).willReturn(Optional.of(follower));
        given(userRepository.findById(followedId)).willReturn(Optional.empty());

        // Act
        var hasBeenRemoved = underTest.removeFollow(followerId, followedId);

        // Assert
        assertThat(hasBeenRemoved).isFalse();
    }

    @Test
    void removeFollow_givenNotExistingFollower_shouldReturnFalse() {
        // Arrange
        var followerId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var followedId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        given(userRepository.findById(followerId)).willReturn(Optional.empty());

        // Act
        var hasBeenRemoved = underTest.removeFollow(followerId, followedId);

        // Assert
        assertThat(hasBeenRemoved).isFalse();
    }

    @Test
    void getUsersIdAndFollowedIds_givenUserWithOneFollowed_shouldReturnListWithUserIdAndFollowedId() {
        // Arrange
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var followedId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        var followedUser = new User(
                followedId,
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

        var followedList = new ArrayList<User>();
        followedList.add(followedUser);

        var user = new User(
                userId,
                "",
                "",
                "",
                "",
                "",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                followedList
        );

        // Act
        var result = underTest.getUsersIdAndFollowedIds(user);

        // Assert
        assertThat(result.size()).isEqualTo(2);

        assertThat(result.stream()
                .filter((uuid) ->
                        uuid.equals(userId))
                .findFirst())
                .isPresent();

        assertThat(result.stream()
                .filter((uuid) ->
                        uuid.equals(followedId))
                .findFirst())
                .isPresent();
    }

    @Test
    void getUsersSuggestions_givenExistingUser_shouldReturnSuggestions() {
        // Arrange
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        var user = new User(
            userId,
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

        var spy = spy(underTest);

        var parameter = new ArrayList<UUID>();

        var expectedSuggestions = new ArrayList<User>();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        doReturn(parameter).when(spy).getUsersIdAndFollowedIds(user);

        given(userRepository.findByUserIdNotIn(parameter)).willReturn(expectedSuggestions);

        // Act
        var optionalActualSuggestions = spy.getUsersSuggestions(userId);

        // Assert
        var actualSuggestions = optionalActualSuggestions.get();
        assertThat(actualSuggestions).isSameAs(expectedSuggestions);

        verify(spy).getUsersIdAndFollowedIds(user);
        verify(userRepository).findByUserIdNotIn(parameter);
    }

    @Test
    void getUsersSuggestions_givenNotExistingUser_shouldReturnEmptyOptional() {
        // Arrange
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // Act
        var optionalSuggestions = underTest.getUsersSuggestions(userId);

        // Assert
        assertThat(optionalSuggestions).isEmpty();
    }

    @Test
    void changeProfileImage_givenExistingUserAndNullProfileImageId_shouldUploadImageAndReturnUpdatedUser() {
        // Arrange
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        var user = new User(
                userId,
                "fullName",
                "username",
                "email",
                "encodedPassword",
                null,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        var imageDataUri = "imageDataUri";
        var newPublicProfileImageId = "newPublicProfileImageId";

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(cloudinaryService.uploadImage(imageDataUri, ImageType.PROFILE_IMAGE, userId))
                .willReturn(newPublicProfileImageId);

        // Act
        var optionalUpdatedUser = underTest.changeProfileImage(userId, imageDataUri);

        // Assert
        var updatedUser = optionalUpdatedUser.get();

        assertThat(updatedUser).isSameAs(user);
        assertThat(updatedUser.getPublicProfileImageId()).isEqualTo(newPublicProfileImageId);

        verify(cloudinaryService).uploadImage(imageDataUri, ImageType.PROFILE_IMAGE, userId);
        verify(cloudinaryService, never()).deleteImage(anyString());
    }

    @Test
    void changeProfileImage_givenExistingUserAndExistingProfileImageId_shouldDeleteOldImageAndUploadNewImageAndReturnUpdatedUser() {
        // Arrange
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var publicProfileImageId = "publicProfileImageId";

        var user = new User(
                userId,
                "fullName",
                "username",
                "email",
                "encodedPassword",
                publicProfileImageId,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        var imageDataUri = "imageDataUri";
        var newPublicProfileImageId = "newPublicProfileImageId";

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(cloudinaryService.uploadImage(imageDataUri, ImageType.PROFILE_IMAGE, userId))
                .willReturn(newPublicProfileImageId);

        // Act
        var optionalUpdatedUser = underTest.changeProfileImage(userId, imageDataUri);

        // Assert
        var updatedUser = optionalUpdatedUser.get();

        assertThat(updatedUser).isSameAs(user);
        assertThat(updatedUser.getPublicProfileImageId()).isEqualTo(newPublicProfileImageId);

        verify(cloudinaryService).uploadImage(imageDataUri, ImageType.PROFILE_IMAGE, userId);
        verify(cloudinaryService).deleteImage(publicProfileImageId);
    }

    @Test
    void changeProfileImage_givenNotExistingUser_shouldReturnEmptyOptional() {
        // Arrange
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // Act
        var optionalUpdatedUser = underTest.changeProfileImage(userId, "imageDataUri");

        // Assert
        assertThat(optionalUpdatedUser).isEmpty();
    }

    @Test
    void changeProfileImage_givenExistingUserAndExistingProfileImageIdButInvalidImage_shouldTrowAndNotDeleteOldImage() {
        // Arrange
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        var imageDataUri = "imageDataUri";

        given(cloudinaryService.uploadImage(imageDataUri, ImageType.PROFILE_IMAGE, userId))
                .willThrow();

        // Act
        // Assert
        assertThatThrownBy(() ->
                underTest.changeProfileImage(userId, imageDataUri));

        verify(cloudinaryService, never()).deleteImage(anyString());
    }
}