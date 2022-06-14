package com.example.kpi.socialnetwork.restControllers;

import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.repository.PostRepository;
import com.example.kpi.socialnetwork.service.PostService;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/posts")
public class PostApiController {

    private final UserService userService;
    private final PostService postService;

    @Autowired
    public PostApiController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping("/save/{id}")
    public boolean savePost(@PathVariable(value = "id") Long postId) {
        User loggedInUser = userService.getLoggedInUser();
        return userService.savePost(loggedInUser, postId) != null;
    }

    @DeleteMapping("/{id}")
    public boolean deletePost(@PathVariable(value = "id") Long postId)
    {
        try
        {
            return postService.deletePost(postId);
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    @PostMapping("retweet/{id}")
    public boolean retweet(@PathVariable(value = "id") Long postId)
    {
        try
        {
            return postService.retweetPost(userService.getLoggedInUser(), postId);
        }
        catch (IOException ex)
        {
            return false;
        }
    }
}
