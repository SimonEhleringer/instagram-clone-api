package com.simonehleringer.instagramcloneapi.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionsResponse {
    private List<UserResponse> suggestions;
}
