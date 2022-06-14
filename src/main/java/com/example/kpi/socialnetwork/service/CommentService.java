package com.example.kpi.socialnetwork.service;

import com.example.kpi.socialnetwork.common.UserComment;
import com.example.kpi.socialnetwork.model.Comment;

public interface CommentService {
    Comment createComment(Comment comment, Long postId);
    UserComment createComment(Long postId, String content);
}
