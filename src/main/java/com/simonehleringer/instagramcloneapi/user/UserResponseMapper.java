package com.simonehleringer.instagramcloneapi.user;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {
    UserResponse toUserResponse(User user);
}
