package com.example.kpi.socialnetwork.service;

import com.example.kpi.socialnetwork.model.Friendship;
import com.example.kpi.socialnetwork.model.User;

import java.util.List;

public interface FriendshipService {
    List<Friendship> getFollowersOfUser(Long userId);

    List<Friendship> getFollowingsOfUser(Long userId);

    Friendship follow(User loggedInUser, User user);
}
