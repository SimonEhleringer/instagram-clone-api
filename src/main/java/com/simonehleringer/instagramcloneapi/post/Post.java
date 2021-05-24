package com.simonehleringer.instagramcloneapi.post;

import com.simonehleringer.instagramcloneapi.user.User;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static com.simonehleringer.instagramcloneapi.post.PostConstants.TEXT__SIZE_MAX;

@Entity
@Data
public class Post {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer postId;

    // TODO: Validation -> length
    @NotNull
    private String publicImageId;

    @Size(max = TEXT__SIZE_MAX)
    private String text;

    @Column(nullable = false)
    private LocalDateTime creationTime;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
