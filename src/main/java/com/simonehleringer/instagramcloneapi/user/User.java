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

@Entity
@Table(name = "appUser")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    // TODO: Validation, Constants for validation
    // TODO: Validation messages
    // TODO: Test validation
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID userId;

    // TODO: Maybe NotNull -> Check what happens to API response if fullName ist null
    @Size(max = 30)
    private String fullName;

    @Column(nullable = false)
    @Size(max = 30)
    // Only characters, numbers and underscore
    @Pattern(regexp = "^[a-zA-Z0-9_]*$")
    private String username;

    // TODO: Check, how long database column can be
    @Column(nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    @Size(max = 60)
    private String encodedPassword;

    // TODO: Maybe NotNull -> Check what happens to API response if fullName ist null
    @Size(max = 150)
    private String characteristics;


    // TODO: Validation -> Length
    private String profileImageLocation;

    @OneToMany(mappedBy = "user")
    private List<RefreshToken> refreshTokens;

    public User(@Size(max = 30) String fullName, @Size(max = 30) @Pattern(regexp = "^[a-zA-Z0-9_]*$") String username, @Email String email) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
    }

    public User(@Size(max = 30) String fullName, @Size(max = 30) @Pattern(regexp = "^[a-zA-Z0-9_]*$") String username, @Email String email, @Size(max = 60) String encodedPassword, @Size(max = 150) String characteristics, String profileImageLocation) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.characteristics = characteristics;
        this.profileImageLocation = profileImageLocation;
    }
}
