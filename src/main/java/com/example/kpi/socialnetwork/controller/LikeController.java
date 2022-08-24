package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.common.UserPost;
import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.service.FriendshipService;
import com.example.kpi.socialnetwork.service.LikeService;
import com.example.kpi.socialnetwork.service.PostService;
import com.example.kpi.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * A controller class for a Like entity
 * */
@Controller
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    private final UserService userService;
    private final PostService postService;
    private final FriendshipService friendshipService;

    /**
     * Like a comment
     * @param id is an ID of a comment
     * */
    @GetMapping("/comment/{id}")
    public String likeComment(@PathVariable Long id, Model model) {
        likeService.addLikeToComment(id, userService.getLoggedInUser().getId());
        List<UserPost> posts = postService.getAllPosts();
        model.addAttribute("postsList", posts);
        model.addAttribute("user", userService.getLoggedInUser());
        model.addAttribute("registeredUsers", userService.findAllExceptCurrent());
        model.addAttribute("tweet", new Post());
        return "posts";
    }

    /**
     * Like a post
     * @param id is an ID of a post
     * */
    @GetMapping("/post/{id}")
    public String likePost(@PathVariable Long id) {
        likeService.addLikeToPost(id, userService.getLoggedInUser().getId());
        return "redirect:/likes";
    }

    /**
     * Retrieve likes of a user
     * */
    @GetMapping
    public String getLikes(Model model) {
        var user = userService.getLoggedInUser();
        List<UserPost> likes = postService.getLikedPostsOfUser(user.getEmail());
        model.addAttribute("postsList", likes);
        model.addAttribute("user", user);
        model.addAttribute("currentUser", user);
        model.addAttribute("registeredUsers", friendshipService.getUsersToFollow());
        model.addAttribute("tweet", new Post());
        return "posts";
    }
}
