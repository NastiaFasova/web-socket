package com.example.kpi.socialnetwork.service.impl;

import com.example.kpi.socialnetwork.model.Comment;
import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.repository.CommentRepository;
import com.example.kpi.socialnetwork.repository.PostRepository;
import com.example.kpi.socialnetwork.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Comment createComment(Comment comment, Long postId) {
        Comment savedComment = Comment.builder()
                .username(comment.getUsername())
                .localDateTime(LocalDateTime.now())
                .content(comment.getContent())
                .likes(new ArrayList<>()).build();
        commentRepository.save(savedComment);
        Post post = postRepository.findByIdFetchComments(postId).orElseThrow();
        post.getComments().add(savedComment);
        postRepository.save(post);
        return comment;
    }
}
