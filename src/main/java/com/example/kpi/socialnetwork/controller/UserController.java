package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.service.PostService;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/me")
    public String myPosts(Model model) throws NullPointerException {
        User user = userService.getLoggedInUser();
        List<Post> posts = postService.getPostsOfUser(user.getId());
        model.addAttribute("posts", posts);
        model.addAttribute("user", user);
        return "index";
    }
}
