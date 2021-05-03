package com.simonehleringer.instagramcloneapi.authentication;

import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.AccessAndRefreshToken;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccessAndRefreshTokenResponseMapper {
    AccessAndRefreshTokenResponseMapper MAPPER = Mappers.getMapper(AccessAndRefreshTokenResponseMapper.class);

    AccessAndRefreshTokenResponse toAccessAndRefreshTokenResponse(AccessAndRefreshToken accessAndRefreshToken);
}
