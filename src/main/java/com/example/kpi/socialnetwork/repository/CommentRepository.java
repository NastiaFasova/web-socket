package com.example.kpi.socialnetwork.repository;

import com.example.kpi.socialnetwork.model.Comment;
import com.example.kpi.socialnetwork.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.likes where c.id =:id")
    Optional<Comment> findById(Long id);
}
