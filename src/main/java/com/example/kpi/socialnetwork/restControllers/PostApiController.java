package com.example.kpi.socialnetwork.restControllers;

import com.example.kpi.socialnetwork.model.Responses.PostDeleteResponse;
import com.example.kpi.socialnetwork.model.Responses.ResultResponse;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.service.PostService;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("api/posts")
public class PostApiController {

    private final UserService userService;
    private final PostService postService;
    private final HttpHeaders httpHeaders;
    private final SimpMessagingTemplate messageTemplate;

    @Autowired
    public PostApiController(UserService userService, PostService postService,
                             SimpMessagingTemplate messageTemplate) {
        this.userService = userService;
        this.postService = postService;
        this.messageTemplate = messageTemplate;
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    @PostMapping("/save/{id}")
    public ResponseEntity<String> savePost(@PathVariable(value = "id") Long postId) {
        User loggedInUser = userService.getLoggedInUser();
        var result = userService.savePost(loggedInUser, postId) != null;
        messageTemplate.convertAndSend("/topic/tweets/save", new ResultResponse(result, postId));
        return new ResponseEntity<>(Boolean.toString(result), httpHeaders, HttpStatus.OK);
    }

    @MessageMapping("/tweets/delete")
    @SendTo("/topic/tweets/delete")
    public PostDeleteResponse deletePost(String postId, Principal principal) {
        try {
            return new PostDeleteResponse(Long.valueOf(postId), postService.deletePost(Long.valueOf(postId),
                    principal.getName()));
        } catch (Exception ex) {
            return new PostDeleteResponse(Long.valueOf(postId), false);
        }
    }

    @PutMapping("/create")
    public ResponseEntity<Long> createPost(@RequestParam("tweet-image") MultipartFile postImage,
                             @RequestParam("content") String content) throws Exception {
        var post = postService.createPost(content, postImage);
        return new ResponseEntity<>(post.getId(), HttpStatus.OK);
    }
}
