package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.service.LikeService;
import com.example.kpi.socialnetwork.service.PostService;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/likes")
public class LikeController {
    private final LikeService likeService;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public LikeController(LikeService likeService, UserService userService,
                          PostService postService) {
        this.likeService = likeService;
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/comment/{id}")
    public String likeComment(@PathVariable Long id, Model model) {
        likeService.addLikeToComment(id, userService.getLoggedInUser().getId());
        List<Post> posts = postService.getAllPosts();
        List<User> authors = userService.getAuthors(posts);
        model.addAttribute("posts", posts);
        model.addAttribute("authors", authors);
        model.addAttribute("user", userService.getLoggedInUser());
        model.addAttribute("registeredUsers", userService.findAll());
        model.addAttribute("tweet", new Post());
        return "posts";
    }

    @GetMapping("/post/{id}")
    public String likePost(@PathVariable Long id) {
        likeService.addLikeToPost(id, userService.getLoggedInUser().getId());
        return "profile";
    }

    @GetMapping
    public String getLikes(Model model) {
        List<Post> likes = postService.getLikedPostsOfUser(userService.getLoggedInUser().getEmail());
        model.addAttribute("posts", likes);
        List<User> authors = userService.getAuthors(likes);
        model.addAttribute("authors", authors);
        model.addAttribute("user", userService.getLoggedInUser());
        model.addAttribute("registeredUsers", userService.findAll());
        model.addAttribute("tweet", new Post());
        return "posts";
    }
}
