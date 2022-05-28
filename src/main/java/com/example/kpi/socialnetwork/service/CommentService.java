package com.example.kpi.socialnetwork.service;

import com.example.kpi.socialnetwork.model.Comment;

public interface CommentService {
    Comment createComment(Comment commentCreateBindingModel) throws Exception;
}
