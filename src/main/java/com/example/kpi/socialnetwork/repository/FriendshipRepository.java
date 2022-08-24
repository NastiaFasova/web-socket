package com.example.kpi.socialnetwork.repository;

import com.example.kpi.socialnetwork.model.Friendship;
import com.example.kpi.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository implementation for Friendship Entity
 * There are CRUD operations by default:
 * Create
 * Read
 * Update
 * Delete
 * */
@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    /**
     * Retrieving followers of user by id
     * */
    @Query("select f.userSender from Friendship f where f.userReceiver.id =:userId")
    List<User> findAllFollowersOfUser(Long userId);

    /**
     * Retrieving followings of user by id
     * */
    @Query("select f.userReceiver from Friendship f where f.userSender.id =:userId")
    List<User> findAllFollowingsOfUser(Long userId);

    /**
     * Retrieving friendships
     * */
    @Query("select f from Friendship f where f.userSender.id =:senderId or f.userReceiver.id =:receiverId")
    Friendship findExistingFriendships(Long senderId, Long receiverId);
}
