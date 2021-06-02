package com.simonehleringer.instagramcloneapi.post.me;

import com.simonehleringer.instagramcloneapi.post.Post;
import com.simonehleringer.instagramcloneapi.post.PostResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FeedResponseMapper {
    default FeedResponse toFeedResponse(List<Post> posts) {
        var mapper = Mappers.getMapper(PostResponseMapper.class);

        return new FeedResponse(
                mapper.toPostResponseList(posts)
        );
    }
}
