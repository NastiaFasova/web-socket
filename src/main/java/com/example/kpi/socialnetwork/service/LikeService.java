package com.example.kpi.socialnetwork.service;

import com.example.kpi.socialnetwork.model.Like;

public interface LikeService {
    Like addLikeToPost(Long postId, Long loggedInUserId);

    Like addLikeToComment(Long postId, Long loggedInUserId);
}
