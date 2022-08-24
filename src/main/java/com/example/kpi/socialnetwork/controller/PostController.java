package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.common.UserPost;
import com.example.kpi.socialnetwork.exceptions.PostException;
import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.Responses.PostEditResponse;
import com.example.kpi.socialnetwork.model.Responses.PostResponse;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.model.dto.PostDto;
import com.example.kpi.socialnetwork.model.dto.PostEditDto;
import com.example.kpi.socialnetwork.model.json.PostLight;
import com.example.kpi.socialnetwork.model.json.UserLight;
import com.example.kpi.socialnetwork.repository.PostRepository;
import com.example.kpi.socialnetwork.repository.UserRepository;
import com.example.kpi.socialnetwork.service.FriendshipService;
import com.example.kpi.socialnetwork.service.PostService;
import com.example.kpi.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Locale;

/**
 * A controller class for a Post entity
 * */
@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final SpringTemplateEngine templateEngine;
    private final SimpMessagingTemplate messageTemplate;

    /**
     * Save a post into DB
     * @param post is an object of post that will be saved
     * */
    @PostMapping("/tweet")
    public String addPost(@ModelAttribute("tweet") Post post) throws NullPointerException {
        User user = userService.getLoggedInUser();
        postService.savePost(user.getEmail(), post);
        return "redirect:/me";
    }

    /**
     * Save a post into DB with new features
     * @param postImage is an image attached to post
     * @param content is a text of a post
     * */
    @PutMapping("/tweet/create")
    public String createPost(Model model, @RequestParam("tweet-image")MultipartFile postImage,
                             @RequestParam("content") String content) throws NullPointerException, IOException {
        var post = postService.createPost(content, postImage);
        model.addAttribute("post", post);
        model.addAttribute("postAuthor", post.getAuthor());
        model.addAttribute("currentUser", post.getAuthor());
        return "fragments/post";
    }

    /**
     * Retrieve all saved posts
     * */
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

    /**
     * Retrieve all posts
     * */
    @GetMapping("/posts")
    public String getAllPosts(Model model){
        model.addAttribute("postsList", postService.getAllPosts());
        model.addAttribute("currentUser", userService.getLoggedInUser());
        model.addAttribute("registeredUsers", friendshipService.getUsersToFollow());
        model.addAttribute("tweet", new Post());
        return "posts";
    }

    @PostMapping(value = "/posts/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getPost(Model model, @PathVariable("id") Long id) {
        User user = userService.getLoggedInUser();
        var post = postService.findById(id);
        model.addAttribute("post", post);
        model.addAttribute("currentUser", user);
        model.addAttribute("postAuthor", user);
        return "fragments/new-post :: post";

    }

    @PostMapping("/save/{id}")
    public boolean savePost(@PathVariable(value = "id") Long postId) {
        User loggedInUser = userService.getLoggedInUser();
        return userService.savePost(loggedInUser, postId) != null;
    }

    /**
     * Retweet a post
     * */
    @GetMapping("/retweet/{id}")
    public String retweetPost(@PathVariable(value = "id") Long postId, Model model) throws IOException {
        User loggedInUser = userService.getLoggedInUser();
        postService.retweetPost(loggedInUser, postId);
        List<UserPost> posts = postService.getPostsOfUser(loggedInUser.getId());
        model.addAttribute("posts", posts);
        return "redirect:/posts";
    }

    /**
     * Delete a post
     * */
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
        throw new PostException("Post not found by ID");
    }

    /**
     * Display a form for editing a post
     * */
    @GetMapping("/edit/{id}/dialog")
    public String editPost(@PathVariable(value = "id") Long postId, Model model) {
        var currentUser = userService.getLoggedInUser();
        var post = postService.findById(postId);
        if (post != null && currentUser.getId().equals(post.getAuthor().getId())) {
            model.addAttribute("post", post);
            return "fragments/post-edit-modal :: post-edit-modal";
        }
        throw new PostException("Post not found by ID");
    }

    /**
     * Save the edited post
     * */
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
        throw new PostException("Post not found by ID");
    }

    @PostMapping("/retweet/{id}")
    public String retweet(@PathVariable("id") Long postId, Model model) throws IOException {
        var post = postService.retweetPost(userService.getLoggedInUser(), postId);
        var postLight = new PostLight(post);

        var context = new Context(Locale.US);
        context.setVariable("post", postLight);
        context.setVariable("currentUser", postLight.getAuthor());
        context.setVariable("postAuthor", postLight.getAuthor());
        var html = templateEngine.process("fragments/new-post", context);
        messageTemplate.convertAndSend("/topic/tweets", new PostResponse(html, post.getAuthor().getId(), post.getId()));
        messageTemplate.convertAndSend("/topic/tweets/user", new PostResponse(html, post.getAuthor().getId(), post.getId()));

        model.addAttribute("post", postLight);
        model.addAttribute("currentUser", postLight.getAuthor());
        model.addAttribute("postAuthor", postLight.getAuthor());
        return "fragments/new-post";
    }

    /**
     * Implementing endpoint for edited posts instant displaying
     * */
    @MessageMapping("/tweets/edit")
    @SendTo("/topic/tweets/edit")
    public PostEditResponse send(PostEditDto postDto)
    {
        return new PostEditResponse(postDto);
    }

    /**
     * Implementing endpoint for all posts instant displaying
     * */
    @MessageMapping("/tweets")
    @SendTo({"/topic/tweets", "/topic/tweets/user"})
    public PostResponse send(PostDto postDto, Principal principal) {
        var user = userService.getByEmail(principal.getName());
        var post = postRepository.findById(postDto.getId()).orElse(null);
        var postLight = new PostLight(post, new UserLight(user));

        var context = new Context(Locale.US);
        context.setVariable("post", postLight);
        context.setVariable("currentUser", postLight.getAuthor());
        context.setVariable("postAuthor", postLight.getAuthor());
        var html = templateEngine.process("fragments/new-post", context);
        return new PostResponse(html, user.getId(), post.getId());
    }
}
