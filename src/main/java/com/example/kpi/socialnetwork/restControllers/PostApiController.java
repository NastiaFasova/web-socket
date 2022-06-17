package com.example.kpi.socialnetwork.restControllers;

import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.repository.PostRepository;
import com.example.kpi.socialnetwork.service.PostService;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/posts")
public class PostApiController {

    private final UserService userService;
    private final PostService postService;
    private final HttpHeaders httpHeaders;

    @Autowired
    public PostApiController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    @PostMapping("/save/{id}")
    public ResponseEntity<String> savePost(@PathVariable(value = "id") Long postId) {
        User loggedInUser = userService.getLoggedInUser();
        return new ResponseEntity<>(Boolean.toString(userService.savePost(loggedInUser, postId) != null), httpHeaders, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(value = "id") Long postId)
    {
        try
        {
            return new ResponseEntity<>(Boolean.toString(postService.deletePost(postId)), httpHeaders, HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>("false",httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "retweet/{id}")
    public ResponseEntity<String> retweet(@PathVariable(value = "id") Long postId)
    {
        try
        {
            return new ResponseEntity<>(Boolean.toString(postService.retweetPost(userService.getLoggedInUser(), postId)),httpHeaders, HttpStatus.OK);
        }
        catch (IOException ex)
        {
            return new ResponseEntity<>("false",httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
