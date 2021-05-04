package com.simonehleringer.instagramcloneapi.user;

import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.refreshToken.RefreshToken;
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
    // TODO: Write tests for validation
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
    // Only characters, numbers and underscore
    @Pattern(regexp = USERNAME__PATTERN_REGEXP)
    private String username;

    @NotNull
    @Email
    // Size is needed for database creation
    @Size(max = EMAIL__SIZE_MAX)
    private String email;

    // Column annotation, because in code password can be null
    @Column(nullable = false)
    @Size(max = ENCODED_PASSWORD__SIZE_MAX)
    private String encodedPassword;

    @NotNull
    @Size(max = CHARACTERISTICS__SIZE_MAX)
    private String characteristics;

    // TODO: Validation -> Length
    private String profileImageLocation;

    @OneToMany(mappedBy = "user")
    private List<RefreshToken> refreshTokens;

    public User(@NotNull @Size(max = FULL_NAME__SIZE_MAX) String fullName, @NotNull @Size(max = USERNAME__SIZE_MAX) @Pattern(regexp = USERNAME__PATTERN_REGEXP) String username, @NotNull @Email @Size(max = EMAIL__SIZE_MAX) String email, @NotNull @Size(max = CHARACTERISTICS__SIZE_MAX) String characteristics) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.characteristics = characteristics;
    }

    public User(@Size(max = FULL_NAME__SIZE_MAX) String fullName, @Size(max = USERNAME__SIZE_MAX) @Pattern(regexp = USERNAME__PATTERN_REGEXP) String username, @Email String email, @Size(max = ENCODED_PASSWORD__SIZE_MAX) String encodedPassword, @Size(max = CHARACTERISTICS__SIZE_MAX) String characteristics, String profileImageLocation) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.characteristics = characteristics;
        this.profileImageLocation = profileImageLocation;
    }
}
