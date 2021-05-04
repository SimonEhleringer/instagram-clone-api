package com.simonehleringer.instagramcloneapi.authentication;

import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.AccessAndRefreshToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccessAndRefreshTokenResponseMapper {
    AccessAndRefreshTokenResponse toAccessAndRefreshTokenResponse(AccessAndRefreshToken accessAndRefreshToken);
}
