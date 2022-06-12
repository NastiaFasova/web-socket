package com.example.kpi.socialnetwork.service;

import com.example.kpi.socialnetwork.model.Comment;

public interface CommentService {
    Comment createComment(Comment comment, Long postId);
    Comment createComment(Long postId, String content);
}
