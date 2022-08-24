package com.example.kpi.socialnetwork.repository;

import com.example.kpi.socialnetwork.model.Like;
import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository implementation for Like Entity
 * There are CRUD operations by default:
 * Create
 * Read
 * Update
 * Delete
 * */
@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {
}
