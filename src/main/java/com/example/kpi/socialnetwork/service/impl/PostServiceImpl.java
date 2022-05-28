package com.example.kpi.socialnetwork.service.impl;

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
    public Post savePost(User user, String content) {
        Post post = new Post();
        User userByEmail = userRepository.findByEmail(user.getEmail());
        post.setUser(userByEmail);
        post.setContent(content);
        return postRepository.save(post);
    }

    @Override
    public List<Post> getPostsOfUser(Long userId) {
        return postRepository.findByUserOrderById(userRepository.findById(userId).orElseThrow());
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByIdDesc();
    }
}
