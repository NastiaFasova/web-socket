package com.example.kpi.socialnetwork.restControllers;

import com.example.kpi.socialnetwork.model.Responses.CommentLikeResponse;
import com.example.kpi.socialnetwork.model.Responses.PostLikeResponse;
import com.example.kpi.socialnetwork.service.CommentService;
import com.example.kpi.socialnetwork.service.LikeService;
import com.example.kpi.socialnetwork.service.PostService;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api/likes")
public class LikeApiController {

    @Autowired
    private LikeService _likeService;

    @Autowired
    private UserService _userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @MessageMapping("/comments/like")
    @SendTo({"/topic/comments/like"})
    public CommentLikeResponse LikeComment(String commentId, Principal principal)
    {
        var currentUser = _userService.getByEmail(principal.getName());
        var result = _likeService.addLikeToComment(Long.valueOf(commentId), currentUser.getId());
        return new CommentLikeResponse(result != null, Long.valueOf(commentId));
    }

    @MessageMapping("/tweets/like")
    @SendTo({"/topic/tweets/like"})
    public PostLikeResponse LikePost(String postId, Principal principal)
    {
        var currentUser = _userService.getByEmail(principal.getName());
        var result = _likeService.addLikeToPost(Long.valueOf(postId), currentUser.getId());
        return new PostLikeResponse(result != null, Long.valueOf(postId));
    }
}
