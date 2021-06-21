package com.simonehleringer.instagramcloneapi.post.me;

import com.simonehleringer.instagramcloneapi.post.Post;
import com.simonehleringer.instagramcloneapi.user.UserResponseMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UserResponseMapper.class }, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface FeedPostResponseMapper {
    @Mapping(source = "user", target = "creator")
    FeedPostResponse toFeedPostResponse(Post post);

    List<FeedPostResponse> toFeedPostResponseList(List<Post> feed);
}
