package com.example.kpi.socialnetwork.service.impl;

import com.example.kpi.socialnetwork.model.Friendship;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.repository.FriendshipRepository;
import com.example.kpi.socialnetwork.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FriendshipServiceImpl implements FriendshipService {
    private final FriendshipRepository friendshipRepository;

    @Autowired
    public FriendshipServiceImpl(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    public List<Friendship> getFollowersOfUser(Long userId) {
        return friendshipRepository.findAllFollowersOfUser(userId);
    }

    @Override
    public List<Friendship> getFollowingsOfUser(Long userId) {
        return friendshipRepository.findAllFollowingsOfUser(userId);
    }

    @Override
    public Friendship follow(User loggedInUser, User user) {
        Friendship friendship = friendshipRepository.findExistingFriendships(loggedInUser.getId(), user.getId());
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
