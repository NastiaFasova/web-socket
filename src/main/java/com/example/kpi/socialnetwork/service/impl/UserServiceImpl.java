package com.example.kpi.socialnetwork.service.impl;

import com.example.kpi.socialnetwork.model.Like;
import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.repository.PostRepository;
import com.example.kpi.socialnetwork.repository.UserRepository;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getByEmail(((UserDetails) principal).getUsername());
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.getById(userId);
    }

    @Override
    public List<User> getAuthors(List<Post> posts) {
        List<User> users = userRepository.findAll();
        List<User> authors = new ArrayList<>();
        for (User user : users) {
            for (Post post : posts) {
                for (Post userPost : user.getPosts()) {
                    if (userPost.equals(post)) {
                        authors.add(user);
                    }
                }
            }
        }
        return authors;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User savePost(User user, Long postId) {
        Post originalPost = postRepository.getById(postId);
        user.getSaved().add(originalPost);
        return userRepository.save(user);
    }

    @Override
    public User retweetPost(User user, Long postId) {
        Post originalPost = postRepository.getById(postId);
        Post post = user.getPosts().stream().filter(p -> p.getId().equals(postId)).findFirst().orElse(null);
        if (post == null) {
            Post newPost = Post.builder()
                    .createdTime(LocalDateTime.now())
                    .likes(new ArrayList<>())
                    .comments(new ArrayList<>())
                    .content(originalPost.getContent())
                    .image(originalPost.getImage()).build() ;
            postRepository.save(newPost);
            user.getPosts().add(newPost);
            return userRepository.save(user);
        } throw new RuntimeException();
    }

    @Override
    public User deletePost(User loggedInUser, Long postId) {
        Post originalPost = postRepository.getById(postId);
        Iterator<Post> i = loggedInUser.getPosts().iterator();
        while (i.hasNext()) {
            Post post = i.next();
            if (post.getId().equals(postId)) {
                i.remove();
                break;
            }
        }
        userRepository.save(loggedInUser);
        postRepository.delete(originalPost);
        return loggedInUser;
    }
}
