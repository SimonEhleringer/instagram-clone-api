package com.simonehleringer.instagramcloneapi.user;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UserResponseMapper.class })
public interface FollowersResponseMapper {
    default FollowersResponse toFollowersResponse(List<User> followers) {
        var mapper = Mappers.getMapper(UserResponseMapper.class);

        return new FollowersResponse(
                mapper.toUserResponseList(followers)
        );
    }
}
