package com.example.kpi.socialnetwork.service.impl;

import com.example.kpi.socialnetwork.common.UserPost;
import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.repository.PostRepository;
import com.example.kpi.socialnetwork.repository.UserRepository;
import com.example.kpi.socialnetwork.service.PostService;
import com.example.kpi.socialnetwork.service.UserService;
import com.example.kpi.socialnetwork.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userService = userService;
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
    public UserPost createPost(String content, MultipartFile file) throws IOException {
        var user = userService.getLoggedInUser();
        var newPost = Post.builder()
                .content(content)
                .createdTime(LocalDateTime.now())
                .image(file.getOriginalFilename())
                .comments(new ArrayList<>())
                .likes(new ArrayList<>())
                .build();
        newPost = postRepository.save(newPost);
        user.getPosts().add(newPost);
        user = userRepository.save(user);

        if (newPost.getImage() != null && !newPost.getImage().isEmpty())
        {
            FileUploadUtil.saveFile(String.format("user-photos/posts/%d/", newPost.getId()), newPost.getImage(),file);
        }

        return new UserPost(user, newPost);
    }

    @Override
    public List<Post> getPostsOfUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return user.getPosts();
    }

    @Override
    public List<UserPost> getAllPosts() {
        List<User> users = userRepository.findAll();
        var postsList = new ArrayList<UserPost>();
        for (var user : users)
        {
            for (var post : user.getPosts())
            {
                postsList.add(new UserPost(user, post));
            }
        }
        return postsList.stream()
                .sorted((l, r) -> r.getCreatedTime().compareTo(l.getCreatedTime()))
                .collect(Collectors.toList());
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
