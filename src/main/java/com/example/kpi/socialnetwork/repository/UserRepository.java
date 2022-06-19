package com.example.kpi.socialnetwork.repository;

import com.example.kpi.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.posts where u.email =:email")
    User findByEmail(String email);

    Optional<User> findById(Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.likes where u.id =:id")
    Optional<User> findByIdFetchLikes(Long id);

    @Query(value = "DELETE FROM users_likes l where l.likes_id =:postId and l.user_id=:userId", nativeQuery = true)
    boolean deleteUserLikedPost(Long postId, Long userId);

    @Query(value = "DELETE FROM users_retweeted l where l.retweeted_id =:postId and l.user_id=:userId", nativeQuery = true)
    boolean deleteUserRetweetedPost(Long postId, Long userId);

    @Query(value = "DELETE FROM users_posts l where l.posts_id =:postId and l.user_id=:userId", nativeQuery = true)
    boolean deleteUserPost(Long postId, Long userId);

    @Query(value = "DELETE FROM users_saved l where l.saved_id =:postId and l.user_id=:userId", nativeQuery = true)
    boolean deleteUserSavedPost(Long postId, Long userId);
}
