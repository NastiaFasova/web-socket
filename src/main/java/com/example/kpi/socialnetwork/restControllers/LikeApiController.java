package com.example.kpi.socialnetwork.restControllers;

import com.example.kpi.socialnetwork.service.LikeService;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/likes")
public class LikeApiController {

    @Autowired
    private LikeService _likeService;

    @Autowired
    private UserService _userService;

    @PostMapping("comment/{id}")
    public boolean LikeComment(@PathVariable("id")long commentId)
    {
        var result = _likeService.addLikeToComment(commentId, _userService.getLoggedInUser().getId());
        return result != null;
    }

    @PostMapping("post/{id}")
    public boolean LikePost(@PathVariable("id")long postId)
    {
        var result = _likeService.addLikeToPost(postId, _userService.getLoggedInUser().getId());
        return result != null;
    }
}
