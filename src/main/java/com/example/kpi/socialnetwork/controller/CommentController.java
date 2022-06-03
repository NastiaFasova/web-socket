package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.model.Comment;
import com.example.kpi.socialnetwork.service.CommentService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{id}")
    public String saveComment(@PathVariable(value = "id") Long postId, Comment comment, Authentication authentication) {
        comment.setUsername(authentication.getName());
        commentService.createComment(comment, postId);
        return "redirect:/";
    }
}
