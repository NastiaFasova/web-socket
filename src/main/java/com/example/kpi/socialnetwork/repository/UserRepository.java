package com.example.kpi.socialnetwork.repository;

import com.example.kpi.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository implementation for User Entity
 * There are CRUD operations by default:
 * Create
 * Read
 * Update
 * Delete
 * */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Retrieving user by email
     * */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.posts where u.email =:email")
    User findByEmail(String email);

    /**
     * Retrieving user by ID
     * */
    Optional<User> findById(Long id);

    /**
     * Retrieving user by ID fetching liked posts
     * */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.likes where u.id =:id")
    Optional<User> findByIdFetchLikes(Long id);
}
