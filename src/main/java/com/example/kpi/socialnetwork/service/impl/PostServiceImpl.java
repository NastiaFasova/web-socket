package com.example.kpi.socialnetwork.service.impl;

import com.example.kpi.socialnetwork.model.Like;
import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.repository.PostRepository;
import com.example.kpi.socialnetwork.repository.UserRepository;
import com.example.kpi.socialnetwork.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Post savePost(String email, Post post) {
        User userByEmail = userRepository.findByEmail(email);
        Post newPost = Post.builder()
                .content(post.getContent())
                .image(post.getImage())
                .createdTime(post.getCreatedTime())
                .build();
        postRepository.save(newPost);
        userByEmail.getPosts().add(newPost);
        userRepository.save(userByEmail);
        return post;
    }

    @Override
    public List<Post> getPostsOfUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return user.getPosts();
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByIdDesc();
    }

    @Override
    public Post findById(Long postId) {
        return postRepository.findById(postId).orElseThrow();
    }

    @Override
    public List<Post> getSavedPostsOfUser(String email) {
        User user = userRepository.findByEmail(email);
        return user.getSaved();
    }

    @Override
    public List<Post> getLikedPostsOfUser(String email) {
        User user = userRepository.findByEmail(email);
        return user.getLikes();
    }
}
