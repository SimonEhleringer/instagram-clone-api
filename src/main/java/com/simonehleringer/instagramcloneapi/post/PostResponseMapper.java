package com.simonehleringer.instagramcloneapi.post;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostResponseMapper {
    PostResponse toPostResponse(Post post);

    List<PostResponse> toPostResponseList(List<Post> posts);
}
