package com.beno.social_media_app.post;

// @Projection
public interface PostProjection {
    Long getId();

    Long getCommentsCnt();

    int getReactionsCnt();

    String getPostText();
}
