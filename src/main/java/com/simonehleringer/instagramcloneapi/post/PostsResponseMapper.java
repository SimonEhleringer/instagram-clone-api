package com.simonehleringer.instagramcloneapi.post;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostsResponseMapper {
    default PostsResponse toPostResponses(List<Post> posts) {
        var mapper = Mappers.getMapper(PostResponseMapper.class);

        return new PostsResponse(
            mapper.toPostResponseList(posts)
        );
    }
}
