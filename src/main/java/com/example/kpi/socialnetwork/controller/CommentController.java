package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.model.Comment;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.service.CommentService;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comment")
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

    @PutMapping("/create")
    public String createComment(Model model, @RequestParam("postId") Long postId, @RequestParam("commentContent") String content){
        model.addAttribute("comment", commentService.createComment(postId, content));
        return "fragments/comment";
    }
}
