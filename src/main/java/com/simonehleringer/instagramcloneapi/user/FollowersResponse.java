package com.simonehleringer.instagramcloneapi.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowersResponse {
    private List<UserResponse> followers;
}
