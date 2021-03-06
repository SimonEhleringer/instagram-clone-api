package com.simonehleringer.instagramcloneapi.user;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UserResponseMapper.class })
public interface FollowedResponseMapper {
    default FollowedResponse toFollowedResponse(List<User> followed) {
        var mapper = Mappers.getMapper(UserResponseMapper.class);

        return new FollowedResponse(
            mapper.toUserResponseList(followed)
        );
    }
}
