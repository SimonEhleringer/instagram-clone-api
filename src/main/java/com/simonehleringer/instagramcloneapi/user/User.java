package com.simonehleringer.instagramcloneapi.user;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Data
public class User {
    // TODO: Validation, Constants for validation

    // TODO: Check if generated ID is not nullable field
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

    @NotNull
    @Size(max = 30)
    private String username;

    // TODO: Maybe NotNull and Size are not needed
    @NotNull
    @Size(max = 320)
    private String email;

    @NotNull
    @Size(max = 60)
    private String encodedPassword;

    // TODO: Maybe NotNull -> Check what happens to API response if fullName ist null
    @Size(max = 150)
    private String characteristics;


    // TODO: Validation -> Length
    private String profileImageLocation;
}
