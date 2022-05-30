package com.example.kpi.socialnetwork.repository;

import com.example.kpi.socialnetwork.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("select f.userSender from Friendship f where f.userReceiver.id =:userId")
    List<Friendship> findAllFollowersOfUser(Long userId);

    @Query("select f.userReceiver from Friendship f where f.userSender.id =:userId")
    List<Friendship> findAllFollowingsOfUser(Long userId);

    @Query("select f from Friendship f where f.userSender.id =:senderId and f.userReceiver.id =:receiverId")
    Friendship findExistingFriendships(Long senderId, Long receiverId);
}
