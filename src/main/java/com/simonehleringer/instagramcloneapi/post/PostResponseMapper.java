package com.simonehleringer.instagramcloneapi.post;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostResponseMapper {
    PostResponse toPostResponse(Post post);
}
