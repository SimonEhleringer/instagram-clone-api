package com.simonehleringer.instagramcloneapi.authentication;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AuthenticationResult {
    private String accessToken;
    private String refreshToken;
    private List<String> errors = new ArrayList<>();

    public boolean hasSucceeded() {
        return errors.size() > 0;
    }
}
