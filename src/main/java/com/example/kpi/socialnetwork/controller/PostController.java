package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.service.PostService;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/post")
    public String addPost(Post post) throws NullPointerException {
        User user = userService.getLoggedInUser();
        postService.savePost(user.getEmail(), post);
        return "posts";
    }

    @GetMapping("/my-posts")
    public String myPosts(Model model) throws NullPointerException {
        User user = userService.getLoggedInUser();
        List<Post> posts = postService.getPostsOfUser(user.getId());
        model.addAttribute("posts", posts);
        return "my_posts";
    }

    @GetMapping("/posts")
    public String getAllPosts(Model model){
        List<Post> posts = postService.getAllPosts();
        List<User> authors = userService.getAuthors(posts);
        model.addAttribute("posts", posts);
        model.addAttribute("authors", authors);
        return "posts";
    }

    @GetMapping("/{userId}/posts")
    public String getPostsOfUser(@PathVariable Long userId, Model model){
        List<Post> posts = postService.getPostsOfUser(userId);
        model.addAttribute("posts", posts);
        return "posts";
    }
}
