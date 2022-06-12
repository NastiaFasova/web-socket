package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.common.KeyValuePair;
import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.repository.UserRepository;
import com.example.kpi.socialnetwork.service.PostService;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public PostController(PostService postService, UserService userService, UserRepository userRepository) {
        this.postService = postService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/tweet")
    public String addPost(@ModelAttribute("tweet") Post post) throws NullPointerException {
        User user = userService.getLoggedInUser();
        postService.savePost(user.getEmail(), post);
        return "redirect:/me";
    }

    @PutMapping("/tweet/create")
    public String createPost(Model model, @RequestParam("tweet-image")MultipartFile postImage, @RequestParam("content") String content) throws NullPointerException, IOException {
        var post = postService.createPost(content, postImage);
        model.addAttribute("post", post);
        model.addAttribute("postAuthor", post.getAuthor());
        model.addAttribute("currentUser", post.getAuthor());
        return "fragments/post";
    }

    @GetMapping("/saved")
    public String mySaved(Model model) throws NullPointerException {
        User user = userService.getLoggedInUser();
        List<Post> posts = postService.getSavedPostsOfUser(user.getEmail());
        List<User> authors = userService.getAuthors(posts);
        model.addAttribute("posts", posts);
        model.addAttribute("authors", authors);
        model.addAttribute("currentUser", userService.getLoggedInUser());
        model.addAttribute("tweet", new Post());
        model.addAttribute("registeredUsers", userService.findAll());
        return "posts";
    }

    @GetMapping("/posts")
    public String getAllPosts(Model model){
        model.addAttribute("postsList", postService.getAllPosts());
        model.addAttribute("currentUser", userService.getLoggedInUser());
        model.addAttribute("registeredUsers", userService.findAll().stream().limit(3).collect(Collectors.toList()));
        model.addAttribute("tweet", new Post());
        return "posts";
    }

    @GetMapping("/save/{id}")
    public String savePost(@PathVariable(value = "id") Long postId, Model model) {
        User loggedInUser = userService.getLoggedInUser();
        userService.savePost(loggedInUser, postId);
        List<Post> posts = postService.getPostsOfUser(loggedInUser.getId());
        model.addAttribute("posts", posts);
        return "redirect:/posts";
    }

    @GetMapping("/retweet/{id}")
    public String retweetPost(@PathVariable(value = "id") Long postId, Model model) {
        User loggedInUser = userService.getLoggedInUser();
        userService.retweetPost(loggedInUser, postId);
        List<Post> posts = postService.getPostsOfUser(loggedInUser.getId());
        model.addAttribute("posts", posts);
        return "redirect:/posts";
    }

    @GetMapping("/delete-post/{id}")
    public String deletePost(@PathVariable(value = "id") Long postId, Model model) {
        User loggedInUser = userService.getLoggedInUser();
        Post post = loggedInUser.getPosts().stream().filter(p -> p.getId().equals(postId)).findAny().orElseThrow();
        if (post != null) {
            userService.deletePost(loggedInUser, postId);
            List<Post> posts = postService.getPostsOfUser(loggedInUser.getId());
            model.addAttribute("posts", posts);
            return "redirect:/me";
        }
        throw new RuntimeException();
    }
}
