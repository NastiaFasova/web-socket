package com.example.kpi.socialnetwork.restControllers;

import com.example.kpi.socialnetwork.model.Responses.CommentLikeResponse;
import com.example.kpi.socialnetwork.model.Responses.PostLikeResponse;
import com.example.kpi.socialnetwork.service.CommentService;
import com.example.kpi.socialnetwork.service.LikeService;
import com.example.kpi.socialnetwork.service.PostService;
import com.example.kpi.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * RestController implementation for Like Entity
 * */
@RequiredArgsConstructor
@RestController
@RequestMapping("api/likes")
public class LikeApiController {
    private final LikeService likeService;
    private final UserService userService;

    /**
     * Implementing endpoint for likes of comments instance displaying
     * */
    @MessageMapping("/comments/like")
    @SendTo({"/topic/comments/like"})
    public CommentLikeResponse likeComment(String commentId, Principal principal)
    {
        var currentUser = userService.getByEmail(principal.getName());
        var result = likeService.addLikeToComment(Long.valueOf(commentId), currentUser.getId());
        return new CommentLikeResponse(result != null, Long.valueOf(commentId));
    }

    /**
     * Implementing endpoint for likes of posts instance displaying
     * */
    @MessageMapping("/tweets/like")
    @SendTo({"/topic/tweets/like"})
    public PostLikeResponse likePost(String postId, Principal principal) {
        var currentUser = userService.getByEmail(principal.getName());
        var result = likeService.addLikeToPost(Long.valueOf(postId), currentUser.getId());
        return new PostLikeResponse(result != null, Long.valueOf(postId));
    }
}
