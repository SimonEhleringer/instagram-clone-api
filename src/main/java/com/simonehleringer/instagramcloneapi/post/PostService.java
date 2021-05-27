package com.simonehleringer.instagramcloneapi.post;

import com.simonehleringer.instagramcloneapi.cloudinary.CloudinaryService;
import com.simonehleringer.instagramcloneapi.cloudinary.ImageType;
import com.simonehleringer.instagramcloneapi.common.ValidationService;
import com.simonehleringer.instagramcloneapi.user.UserService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Size;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.simonehleringer.instagramcloneapi.post.PostConstants.TEXT__SIZE_MAX;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CloudinaryService cloudinaryService;
    private final ValidationService validationService;
    private final UserService userService;
    private final Clock clock;

    @Transactional
    public Optional<Post> createPost(String text, String imageDataUri, UUID userId) {
        var postToCreate = new Post();
        postToCreate.setText(text);

        var optionalUser = userService.getById(userId);

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        var user = optionalUser.get();

        postToCreate.setUser(user);
        postToCreate.setCreationTime(LocalDateTime.now(clock));

        validationService.validate(postToCreate);

        var publicImageId = cloudinaryService.uploadImage(imageDataUri, ImageType.POST, userId);

        postToCreate.setPublicImageId(publicImageId);

        var createdPost = postRepository.save(postToCreate);

        return Optional.of(createdPost);
    }
}
