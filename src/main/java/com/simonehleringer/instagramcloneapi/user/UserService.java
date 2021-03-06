package com.simonehleringer.instagramcloneapi.user;

import com.simonehleringer.instagramcloneapi.cloudinary.CloudinaryService;
import com.simonehleringer.instagramcloneapi.cloudinary.ImageType;
import com.simonehleringer.instagramcloneapi.common.ValidationService;
import com.simonehleringer.instagramcloneapi.user.me.CanNotAddFollowException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public User createUser(@NonNull User userToCreate,@NonNull @Pattern(regexp = UserConstants.PASSWORD__PATTERN_REGEXP) String password) {
        validationService.validate(userToCreate);
        validationService.validate(password);

        if (userRepository.existsByUsernameIgnoreCase(userToCreate.getUsername())) {
            throw new CanNotCreateUserException("Dieser Benutzername ist bereits vergeben.");
        }

        if (userRepository.existsByEmailIgnoreCase(userToCreate.getEmail())) {
            throw new CanNotCreateUserException("Diese E-Mail ist bereits vergeben.");
        }

        userToCreate.setEncodedPassword(passwordEncoder.encode(password));

        return userRepository.save(userToCreate);
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    public boolean checkPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getEncodedPassword());
    }

    public Optional<User> getById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    public boolean addFollow(UUID followerId, UUID followedId) {
        if (followerId.equals(followedId)) {
            throw new CanNotAddFollowException("Du kannst dir selbst nicht folgen.");
        }

        var optionalFollower = userRepository.findById(followerId);

        if (optionalFollower.isEmpty()) {
            return false;
        }

        var follower = optionalFollower.get();

        var doesFollowAlreadyExist = follower.getFollowed().stream()
                .anyMatch((user) -> user.getUserId().equals(followedId));

        if (doesFollowAlreadyExist) {
            throw new CanNotAddFollowException("Du folgst diesem Benutzer bereits.");
        }

        var optionalFollowed = userRepository.findById(followedId);

        if (optionalFollowed.isEmpty()) {
            return false;
        }

        var followed = optionalFollowed.get();

        followed.getFollowers().add(follower);
        follower.getFollowed().add(followed);

        return true;
    }

    public Optional<List<User>> getUsersFollowed(UUID userId) {
        var optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        var user = optionalUser.get();

        return Optional.of(user.getFollowed());
    }

    public Optional<List<User>> getUsersFollowers(UUID userId) {
        var optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        var user = optionalUser.get();

        return Optional.of(user.getFollowers());
    }

    @Transactional
    public boolean removeFollow(UUID followerId, UUID followedId) {
        var optionalFollower = userRepository.findById(followerId);

        if (optionalFollower.isEmpty()) {
            return false;
        }

        var follower = optionalFollower.get();

        var optionalFollowed = userRepository.findById(followedId);

        if (optionalFollowed.isEmpty()) {
            return false;
        }

        var followed = optionalFollowed.get();

        var hasBeenRemoved = follower.getFollowed().remove(followed);

        if (!hasBeenRemoved) {
            return false;
        }

        followed.getFollowers().remove(follower);

        return true;
    }

    public Optional<List<User>> getUsersSuggestions(UUID userId) {
        var optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        var user = optionalUser.get();

        var parameter = getUsersIdAndFollowedIds(user);

        var suggestions = userRepository.findByUserIdNotIn(parameter);

        return Optional.of(suggestions);
    }

    public List<UUID> getUsersIdAndFollowedIds(User user) {
        var result = new ArrayList<UUID>();

        result.add(user.getUserId());

        for (var followed : user.getFollowed()) {
            result.add(followed.getUserId());
        }

        return result;
    }

    @Transactional
    public Optional<User> changeProfileImage(UUID userId, String imageDataUri) {
        var optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        var user = optionalUser.get();

        var publicImageId = cloudinaryService.uploadImage(imageDataUri, ImageType.PROFILE_IMAGE, user.getUserId());

        if (user.getPublicProfileImageId() != null) {
            cloudinaryService.deleteImage(user.getPublicProfileImageId());
        }

        user.setPublicProfileImageId(publicImageId);

        return Optional.of(user);
    }
}
