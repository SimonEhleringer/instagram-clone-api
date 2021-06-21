package com.simonehleringer.instagramcloneapi.post.me;

import com.simonehleringer.instagramcloneapi.user.UserResponseMapper;
import org.mapstruct.factory.Mappers;

public class FeedPostResponseMapperUtils {
    public static FeedPostResponseMapper generateFeedPostResponseMapper() {
        try {
            var aClass = Class.forName(FeedPostResponseMapperImpl.class.getCanonicalName());
            var constructor = aClass.getConstructor(UserResponseMapper.class);
            return (FeedPostResponseMapper)constructor.newInstance( Mappers.getMapper(UserResponseMapper.class));
        } catch (Exception e) {
            throw new CanNotGenerateFeedPostResponseMapperException(e);
        }
    }
}
