package com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.refreshToken;

import com.simonehleringer.instagramcloneapi.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
public class RefreshToken {
    @Id
    private String token;

    @Column(nullable = false)
    private LocalDateTime creationTime;

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    @Column(nullable = false)
    private boolean valid;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
