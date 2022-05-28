package com.example.kpi.socialnetwork.service;

public interface LikeService {
    int getAllLikesForPost(Long postId);

    boolean addLike(Long postId, Long loggedInUserId);
}
