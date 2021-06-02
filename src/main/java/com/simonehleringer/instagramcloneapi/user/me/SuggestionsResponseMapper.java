package com.simonehleringer.instagramcloneapi.user.me;

import com.simonehleringer.instagramcloneapi.user.User;
import com.simonehleringer.instagramcloneapi.user.UserResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UserResponseMapper.class })
public interface SuggestionsResponseMapper {
    default SuggestionsResponse toSuggestionsResponse(List<User> suggestions) {
        var mapper = Mappers.getMapper(UserResponseMapper.class);

        return new SuggestionsResponse(
                mapper.toUserResponseList(suggestions)
        );
    }
}
