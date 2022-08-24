package com.example.kpi.socialnetwork.service;

import com.example.kpi.socialnetwork.common.UserComment;
import com.example.kpi.socialnetwork.model.Comment;

/**
 * Service layer for Comment Entity
 * */
public interface CommentService {
    Comment createComment(Comment comment, Long postId);

    UserComment createComment(Long postId, String content);

    UserComment findById(Long commentId);
}
