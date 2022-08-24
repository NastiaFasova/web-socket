package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.model.Comment;
import com.example.kpi.socialnetwork.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * A controller class for a Comment entity
 * */
@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /**
     * Saving a comment into DB
     * @param postId is an ID of post where comment will be created
     * @param comment is a comment that is saved
     * */
    @PostMapping("/{id}")
    public String saveComment(@PathVariable(value = "id") Long postId, Comment comment,
                              Authentication authentication) {
        comment.setUsername(authentication.getName());
        commentService.createComment(comment, postId);
        return "redirect:/";
    }

    /**
     * Displaying a form for creating a comment
     * @param postId is an ID of post where comment will be created
     * @param content is a text of comment
     * */
    @PutMapping("/create")
    public String createComment(Model model, @RequestParam("postId") Long postId,
                                @RequestParam("commentContent") String content){
        model.addAttribute("comment", commentService.createComment(postId, content));
        return "fragments/comment";
    }
}
