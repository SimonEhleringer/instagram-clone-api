package com.simonehleringer.instagramcloneapi.user;

import com.simonehleringer.instagramcloneapi.common.ValidationService;
import com.simonehleringer.instagramcloneapi.common.jwtAuthentication.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Pattern;
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
    public Optional<List<User>> addFollow(UUID followerId, UUID followedId) {
        if (followerId.equals(followedId)) {
            throw new CanNotAddFollowException("Du kannst dir selbst nicht folgen.");
        }

        var optionalFollower = userRepository.findById(followerId);

        if (optionalFollower.isEmpty()) {
            return Optional.empty();
        }

        var follower = optionalFollower.get();

        var doesFollowAlreadyExist = follower.getFollowed().stream()
                .anyMatch((user) -> user.getUserId().equals(followedId));

        if (doesFollowAlreadyExist) {
            throw new CanNotAddFollowException("Du folgst diesem Benutzer bereits.");
        }

        var optionalFollowed = userRepository.findById(followedId);

        if (optionalFollowed.isEmpty()) {
            return Optional.empty();
        }

        var followed = optionalFollowed.get();

        followed.getFollowers().add(follower);
        follower.getFollowed().add(followed);

        return Optional.of(follower.getFollowed());
    }

}
