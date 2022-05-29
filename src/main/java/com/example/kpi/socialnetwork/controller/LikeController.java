package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.service.LikeService;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/like")
public class LikeController {
    private final LikeService likeService;
    private final UserService userService;

    @Autowired
    public LikeController(LikeService likeService, UserService userService) {
        this.likeService = likeService;
        this.userService = userService;
    }

    @GetMapping("/comment/{id}")
    public void likeComment(@PathVariable Long id) {
        likeService.addLikeToComment(id, userService.getLoggedInUser().getId());
    }

    @GetMapping("/post/{id}")
    public void likePost(@PathVariable Long id) {
        likeService.addLikeToPost(id, userService.getLoggedInUser().getId());
    }
}
