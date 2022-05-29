package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.model.Comment;
import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.service.CommentService;
import com.example.kpi.socialnetwork.service.LikeService;
import com.example.kpi.socialnetwork.service.PostService;
import com.example.kpi.socialnetwork.service.RegistrationService;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class InjectDataController {
    private final RegistrationService registrationService;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;

    @Autowired
    public InjectDataController(RegistrationService registrationService,
                                UserService userService, PostService postService,
                                CommentService commentService,
                                LikeService likeService) {
        this.registrationService = registrationService;
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    @PostConstruct
    public void addUsers() {
        User user = User.builder()
                .fullName("William Wider")
                .email("william@gmail.com")
                .fullName("William Wider")
                .password("09092001").build();
        registrationService.register(user);

        Post post = Post.builder()
                .createdTime(LocalDateTime.now())
                .content("This is my first post")
                .image("/img/avatar.jpg")
                .build();
        postService.savePost(user.getEmail(), post);

        Comment comment = Comment.builder()
                .content("Fine!")
                .userEmail("william@gmail.com")
                .username("William Wider")
                .localDateTime(LocalDateTime.now())
                .build();
        commentService.createComment(comment, 1L);

        likeService.addLikeToPost(1L, 1L);
    }
}
