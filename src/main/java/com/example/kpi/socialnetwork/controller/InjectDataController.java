package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.model.Comment;
import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.service.CommentService;
import com.example.kpi.socialnetwork.service.FriendshipService;
import com.example.kpi.socialnetwork.service.LikeService;
import com.example.kpi.socialnetwork.service.PostService;
import com.example.kpi.socialnetwork.service.RegistrationService;
import com.example.kpi.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

/**
 * Injecting some new data into DB for testing purpose
 * */
@Controller
@RequiredArgsConstructor
public class InjectDataController {
    private final RegistrationService registrationService;
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;

    /**
     * Adding new users, posts, comments, likes into DB
     * */
    @PostConstruct
    public void addUsers() {
        User user = User.builder()
                .fullName("William Wider")
                .email("william@gmail.com")
                .password("09092001")
                .confirmPassword("09092001").build();
        registrationService.register(user);

        User user2 = User.builder()
                .fullName("Dusan Borota")
                .email("dusan@gmail.com")
                .password("09092001")
                .confirmPassword("09092001").build();
        registrationService.register(user2);

        friendshipService.follow(2L, 1L);

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
        likeService.addLikeToPost(1L, 1L);
    }
}
