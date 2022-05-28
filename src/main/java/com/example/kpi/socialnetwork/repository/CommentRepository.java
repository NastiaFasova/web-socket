package com.example.kpi.socialnetwork.repository;

import com.example.kpi.socialnetwork.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
