package com.simonehleringer.instagramcloneapi.user;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class User {
    // TODO: Validation

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID userId;

    private String fullName;

    private String username;

    private String email;

    private String encodedPassword;

    private String characteristics;

    private String profileImageLocation;
}
