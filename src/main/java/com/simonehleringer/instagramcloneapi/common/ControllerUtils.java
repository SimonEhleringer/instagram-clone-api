package com.simonehleringer.instagramcloneapi.common;

import com.simonehleringer.instagramcloneapi.common.jwtAuthentication.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

// TODO: Add tests?
public class ControllerUtils {
    public static UUID getLoggedInUserId() {
        return ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
    }
}
