package com.example.kpi.socialnetwork.service.impl;

import com.example.kpi.socialnetwork.common.UserFollow;
import com.example.kpi.socialnetwork.model.Friendship;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.repository.FriendshipRepository;
import com.example.kpi.socialnetwork.repository.UserRepository;
import com.example.kpi.socialnetwork.service.FriendshipService;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendshipServiceImpl implements FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public FriendshipServiceImpl(FriendshipRepository friendshipRepository, UserRepository userRepository, UserService userService) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public List<UserFollow> getFollowersOfUser(Long userId) {
        var userFollowers = friendshipRepository.findAllFollowersOfUser(userId);
        var currentUserFollowings = friendshipRepository.findAllFollowingsOfUser(userService.getLoggedInUser().getId());
        var friendships = friendshipRepository.findAll();

        return userFollowers.stream().map(f ->
                new UserFollow(f,
                        currentUserFollowings.stream().anyMatch(cf -> cf.getId().equals(f.getId())),
                        friendships.stream().filter(friendship -> friendship.getUserReceiver().getId().equals(f.getId())).count())).collect(Collectors.toList());
    }

    @Override
    public List<UserFollow> getFollowingsOfUser(Long userId) {
        var userFollowings = friendshipRepository.findAllFollowingsOfUser(userId);
        var currentUserFollowings = friendshipRepository.findAllFollowingsOfUser(userService.getLoggedInUser().getId());
        var friendships = friendshipRepository.findAll();

        return userFollowings.stream().map(f ->
                new UserFollow(f,
                        currentUserFollowings.stream().anyMatch(cf -> cf.getId().equals(f.getId())),
                        friendships.stream().filter(friendship -> friendship.getUserReceiver().getId().equals(f.getId())).count())).collect(Collectors.toList());
    }

    @Override
    public List<UserFollow> getUsersToFollow() {
        var friendships = friendshipRepository.findAll();
        var currentUserId = userService.getLoggedInUser().getId();
        var currentUserFollowings = friendshipRepository.findAllFollowingsOfUser(currentUserId);
        var users = userRepository.findAll();

        return users.stream().map(u -> new UserFollow(u,
                currentUserFollowings.stream().anyMatch(cf -> cf.getId().equals(u.getId())),
                friendships.stream().filter(f -> f.getUserReceiver().getId().equals(u.getId())).count())
        ).sorted((l, r) -> (int)(r.getFollowersCount() - l.getFollowersCount())).filter(u -> !u.getUser().getId().equals(currentUserId))
                .limit(3).collect(Collectors.toList());
    }

    @Override
    public Friendship follow(Long loggedInUserId, Long userId) {
        Friendship friendship = friendshipRepository.findExistingFriendships(loggedInUserId, userId);
        User loggedInUser = userRepository.findById(loggedInUserId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        if (friendship != null && friendship.getAccepted()) {
            throw new RuntimeException("You are already friends");
        } else if (friendship != null && !friendship.getAccepted()) {
            friendship.setAccepted(true);
            return friendshipRepository.save(friendship);
        }
        friendship = Friendship.builder()
                .userReceiver(user)
                .userSender(loggedInUser)
                .build();
        return friendshipRepository.save(friendship);
    }
}
