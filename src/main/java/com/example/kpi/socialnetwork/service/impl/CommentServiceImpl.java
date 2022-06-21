package com.example.kpi.socialnetwork.service.impl;

import com.example.kpi.socialnetwork.common.UserComment;
import com.example.kpi.socialnetwork.model.Comment;
import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.repository.CommentRepository;
import com.example.kpi.socialnetwork.repository.PostRepository;
import com.example.kpi.socialnetwork.service.CommentService;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Override
    public Comment createComment(Comment comment, Long postId) {
        Comment savedComment = Comment.builder()
                .username(comment.getUsername())
                .localDateTime(LocalDateTime.now())
                .content(comment.getContent())
                .userEmail(comment.getUserEmail())
                .likes(new ArrayList<>()).build();
        commentRepository.save(savedComment);
        Post post = postRepository.findByIdFetchComments(postId).orElseThrow();
        post.getComments().add(savedComment);
        postRepository.save(post);
        return savedComment;
    }

    @Override
    public UserComment createComment(Long postId, String content) {
        var currentUser = userService.getLoggedInUser();
        Comment savedComment = Comment.builder()
                .username(currentUser.getFullName())
                .userEmail(currentUser.getEmail())
                .localDateTime(LocalDateTime.now())
                .content(content)
                .likes(new ArrayList<>()).build();
        savedComment = commentRepository.save(savedComment);
        Post post = postRepository.findByIdFetchComments(postId).orElseThrow();
        post.getComments().add(savedComment);
        postRepository.save(post);

        return new UserComment(savedComment, currentUser);
    }

    @Override
    public UserComment findById(Long commentId) {
        var comment = commentRepository.findById(commentId).orElse(null);
        if (comment != null)
        {
            var user = userService.getByEmail(comment.getUserEmail());

            return new UserComment(comment, user);
        }
        return null;
    }
}
