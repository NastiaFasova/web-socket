package com.example.kpi.socialnetwork.repository;

import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository implementation for Post Entity
 * There are CRUD operations by default:
 * Create
 * Read
 * Update
 * Delete
 * */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    /**
     * Retrieving post by id
     * */
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.likes where p.id =:id")
    Optional<Post> findById(Long id);

    /**
     * Retrieving post by id and fetching its comments
     * */
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments where p.id =:id")
    Optional<Post> findByIdFetchComments(Long id);
}
