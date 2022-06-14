package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.common.UserPost;
import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.repository.UserRepository;
import com.example.kpi.socialnetwork.service.FriendshipService;
import com.example.kpi.socialnetwork.service.PostService;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final FriendshipService friendshipService;

    @Autowired
    public PostController(PostService postService, UserService userService, UserRepository userRepository, FriendshipService friendshipService) {
        this.postService = postService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.friendshipService = friendshipService;
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
        List<UserPost> posts = postService.getSavedPostsOfUser(user.getEmail());
        model.addAttribute("postsList", posts);
        model.addAttribute("tweet", new Post());
        model.addAttribute("registeredUsers", friendshipService.getUsersToFollow());
        model.addAttribute("currentUser", user);
        return "posts";
    }

    @GetMapping("/posts")
    public String getAllPosts(Model model){
        User user = userService.getLoggedInUser();
        model.addAttribute("postsList", postService.getAllPosts());
        model.addAttribute("currentUser", userService.getLoggedInUser());
        model.addAttribute("registeredUsers", friendshipService.getUsersToFollow());
        model.addAttribute("tweet", new Post());
        return "posts";
    }

    @PostMapping("/save/{id}")
    public boolean savePost(@PathVariable(value = "id") Long postId) {
        User loggedInUser = userService.getLoggedInUser();
        return userService.savePost(loggedInUser, postId) != null;
    }

    @GetMapping("/retweet/{id}")
    public String retweetPost(@PathVariable(value = "id") Long postId, Model model) throws IOException {
        User loggedInUser = userService.getLoggedInUser();
        postService.retweetPost(loggedInUser, postId);
        List<UserPost> posts = postService.getPostsOfUser(loggedInUser.getId());
        model.addAttribute("posts", posts);
        return "redirect:/posts";
    }

    @GetMapping("/delete-post/{id}")
    public String deletePost(@PathVariable(value = "id") Long postId, Model model) {
        User loggedInUser = userService.getLoggedInUser();
        Post post = loggedInUser.getPosts().stream().filter(p -> p.getId().equals(postId)).findAny().orElseThrow();
        if (post != null) {
            postService.deletePost(postId);
            List<UserPost> posts = postService.getPostsOfUser(loggedInUser.getId());
            model.addAttribute("postsList", posts);
            return "redirect:/me";
        }
        throw new RuntimeException();
    }

    @GetMapping("/edit/{id}/dialog")
    public String editPost(@PathVariable(value = "id") Long postId, Model model) {
        var post = postService.findById(postId);
        if (post != null) {
            model.addAttribute("post", post);
            return "fragments/post-edit-modal :: post-edit-modal";
        }
        throw new RuntimeException();
    }

    @PostMapping("/edit/{id}")
    public String editPost(@PathVariable(value = "id") Long postId, @RequestParam("edit-tweet") String content, @RequestParam("tweet-image") MultipartFile file, Model model) {
        var loggedInUser = userService.getLoggedInUser();
        var post = postService.editPost(postId, content, file);
        var userPost = postService.findById(post.getId());
        if (post != null) {
            model.addAttribute("post", userPost);
            model.addAttribute("currentUser", loggedInUser);
            model.addAttribute("postAuthor", loggedInUser);
            return "fragments/post :: post";
        }
        throw new RuntimeException();
    }
}
