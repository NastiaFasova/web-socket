package com.example.kpi.socialnetwork.service;

import com.example.kpi.socialnetwork.model.Like;

public interface LikeService {
    int getAllLikesForPost(Long postId);

    Like addLike(Long postId, Long loggedInUserId);

    Like addLikeToComment(Long postId, Long loggedInUserId);
}
