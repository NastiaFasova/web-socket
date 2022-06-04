package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.model.dto.UserRegisterDto;
import com.example.kpi.socialnetwork.service.PostService;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @PostMapping("/tweet")
    public String addPost(@ModelAttribute("tweet") Post post) throws NullPointerException {
        User user = userService.getLoggedInUser();
        postService.savePost(user.getEmail(), post);
        return "redirect:/me";
    }

    @GetMapping("/saved")
    public String mySaved(Model model) throws NullPointerException {
        User user = userService.getLoggedInUser();
        List<Post> posts = postService.getSavedPostsOfUser(user.getEmail());
        List<User> authors = userService.getAuthors(posts);
        model.addAttribute("posts", posts);
        model.addAttribute("authors", authors);
        model.addAttribute("user", userService.getLoggedInUser());
        model.addAttribute("tweet", new Post());
        model.addAttribute("registeredUsers", userService.findAll());
        return "posts";
    }

    @GetMapping("/posts")
    public String getAllPosts(Model model){
        List<Post> posts = postService.getAllPosts();
        List<User> authors = userService.getAuthors(posts);
        model.addAttribute("posts", posts);
        model.addAttribute("authors", authors);
        model.addAttribute("user", userService.getLoggedInUser());
        model.addAttribute("registeredUsers", userService.findAll());
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
