package com.simonehleringer.instagramcloneapi.post.me;

public class CanNotGenerateFeedPostResponseMapperException extends RuntimeException {
    public CanNotGenerateFeedPostResponseMapperException(Throwable cause) {
        super("Unable to generate FeedPostResponseMapper.", cause);
    }
}
