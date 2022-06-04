package com.example.kpi.socialnetwork.service;

import com.example.kpi.socialnetwork.model.Friendship;
import com.example.kpi.socialnetwork.model.User;

import java.util.List;

public interface FriendshipService {
    List<User> getFollowersOfUser(Long userId);

    List<User> getFollowingsOfUser(Long userId);

    Friendship follow(Long loggedInUserId, Long userId);
}
