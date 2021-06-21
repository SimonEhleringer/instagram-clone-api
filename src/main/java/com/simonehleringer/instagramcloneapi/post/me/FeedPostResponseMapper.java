package com.simonehleringer.instagramcloneapi.post.me;

import com.simonehleringer.instagramcloneapi.post.Post;
import com.simonehleringer.instagramcloneapi.user.UserResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UserResponseMapper.class })
public interface FeedPostResponseMapper {
    @Mapping(source = "user", target = "creator")
    FeedPostResponse toFeedPostResponse(Post post);

    List<FeedPostResponse> toFeedPostResponseList(List<Post> feed);
}
