package com.simonehleringer.instagramcloneapi.post.me;

import com.simonehleringer.instagramcloneapi.post.Post;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface FeedResponseMapper {
    default FeedResponse toFeedResponse(List<Post> posts) {
        var mapper = FeedPostResponseMapperUtils.generateFeedPostResponseMapper();

        return new FeedResponse(
                mapper.toFeedPostResponseList(posts)
        );
    }
}
