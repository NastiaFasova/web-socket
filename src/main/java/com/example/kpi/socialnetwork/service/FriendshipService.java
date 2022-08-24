package com.example.kpi.socialnetwork.service;

import com.example.kpi.socialnetwork.common.UserFollow;
import com.example.kpi.socialnetwork.model.Friendship;
import com.example.kpi.socialnetwork.model.User;

import java.util.List;

/**
 * Service layer for Friendship Entity
 * */
public interface FriendshipService {
    List<UserFollow> getFollowersOfUser(Long userId);

    List<UserFollow> getFollowingsOfUser(Long userId);

    List<UserFollow> getUsersToFollow();

    Friendship follow(Long loggedInUserId, Long userId);
}
