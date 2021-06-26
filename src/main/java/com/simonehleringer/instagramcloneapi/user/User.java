package com.simonehleringer.instagramcloneapi.user;

import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.refreshToken.RefreshToken;
import com.simonehleringer.instagramcloneapi.post.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.UUID;

import static com.simonehleringer.instagramcloneapi.user.UserConstants.*;

@Entity
@Table(name = "appUser")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID userId;

    @NotNull
    @Size(max = FULL_NAME__SIZE_MAX)
    private String fullName;

    @NotNull
    @Size(max = USERNAME__SIZE_MAX)
    @Pattern(regexp = USERNAME__PATTERN_REGEXP)
    private String username;

    @NotNull
    @Email
    // Size is needed for database creation
    @Size(max = EMAIL__SIZE_MAX)
    private String email;

    @Column(nullable = false)
    @Size(max = ENCODED_PASSWORD__SIZE_MAX)
    private String encodedPassword;

    @NotNull
    @Size(max = CHARACTERISTICS__SIZE_MAX)
    private String characteristics;

    @Size(max = PUBLIC_PROFILE_IMAGE_ID__SIZE_MAX)
    private String publicProfileImageId;

    @OneToMany(mappedBy = "user")
    private List<RefreshToken> refreshTokens;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="Follow", joinColumns = { @JoinColumn(name="followedId") }, inverseJoinColumns = { @JoinColumn(name = "followerId") })
    private List<User> followers;

    @ManyToMany(mappedBy = "followers", cascade = CascadeType.ALL)
    private List<User> followed;

    public User(@NotNull @Size(max = FULL_NAME__SIZE_MAX) String fullName, @NotNull @Size(max = USERNAME__SIZE_MAX) @Pattern(regexp = USERNAME__PATTERN_REGEXP) String username, @NotNull @Email @Size(max = EMAIL__SIZE_MAX) String email, @NotNull @Size(max = CHARACTERISTICS__SIZE_MAX) String characteristics) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.characteristics = characteristics;
    }

    public User(@Size(max = FULL_NAME__SIZE_MAX) String fullName, @Size(max = USERNAME__SIZE_MAX) @Pattern(regexp = USERNAME__PATTERN_REGEXP) String username, @Email String email, @Size(max = ENCODED_PASSWORD__SIZE_MAX) String encodedPassword, @Size(max = CHARACTERISTICS__SIZE_MAX) String characteristics, String publicProfileImageId) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.characteristics = characteristics;
        this.publicProfileImageId = publicProfileImageId;
    }
}
