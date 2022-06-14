package com.example.kpi.socialnetwork.service.impl;

import com.example.kpi.socialnetwork.common.UserComment;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
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
    public UserPost savePost(String email, Post post) {
        User userByEmail = userRepository.findByEmail(email);
        Post newPost = Post.builder()
                .content(post.getContent())
                .image(post.getImage())
                .createdTime(post.getCreatedTime())
                .build();
        newPost = postRepository.save(newPost);
        userByEmail.getPosts().add(newPost);
        userByEmail = userRepository.save(userByEmail);
        return new UserPost(userByEmail, newPost);
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
    public List<UserPost> getPostsOfUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return convert(user.getPosts());
    }

    @Override
    public List<UserPost> getAllPosts() {
        return convert(null);
    }

    @Override
    public UserPost findById(Long postId) {
        var post = postRepository.findById(postId).orElseThrow();
        var posts = new ArrayList<Post>();
        posts.add(post);
        var res = convert(posts);
        return res.get(0);
    }

    @Override
    public List<UserPost> getSavedPostsOfUser(String email) {
        User user = userRepository.findByEmail(email);
        return convert(user.getSaved());
    }

    @Override
    public List<UserPost> getLikedPostsOfUser(String email) {
        User user = userRepository.findByEmail(email);
        return convert(user.getLikes());
    }

    @Override
    public boolean deletePost(Long postId) {
        var user = userService.getLoggedInUser();
        deletePost(user.getLikes(), postId);
        deletePost(user.getPosts(), postId);
        deletePost(user.getSaved(), postId);
        userRepository.save(user);
        postRepository.deleteById(postId);
        return true;
    }

    @Override
    public Post editPost(long postId, String newContent, MultipartFile file) {
        var post = postRepository.findById(postId).orElse(null);
        if (post == null)
        {
            return null;
        }

        post.setContent(newContent);
        if (file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty() && !post.getImage().equals(file.getOriginalFilename()))
        {
            try
            {
                FileUploadUtil.saveFile(String.format("user-photos/posts/%d/", post.getId()), file.getOriginalFilename(),file);
            }
            catch (Exception ex)
            {
                return null;
            }
            post.setImage(file.getOriginalFilename());
        }
        return postRepository.save(post);
    }

    @Override
    public boolean retweetPost(User user, Long postId) throws IOException {
        Post originalPost = postRepository.getById(postId);
        Post post = user.getPosts().stream().filter(p -> p.getId().equals(postId)).findFirst().orElse(null);
        if (post == null) {
            Post newPost = Post.builder()
                    .createdTime(LocalDateTime.now())
                    .likes(new ArrayList<>())
                    .comments(new ArrayList<>())
                    .content(originalPost.getContent())
                    .image(originalPost.getImage()).build();
            newPost = postRepository.save(newPost);
            if (newPost.getImage() != null && !newPost.getImage().isEmpty())
            {
                try {
                    FileUploadUtil.copyFile(originalPost.getImagePath(), newPost.getImagePath());
                }
                catch (IOException ex)
                {
                    postRepository.deleteById(newPost.getId());
                    return false;
                }
            }

            user.getPosts().add(newPost);
            user.getRetweeted().add(originalPost);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    private void deletePost(List<Post> posts, Long postId){
        Iterator<Post> i = posts.iterator();
        while (i.hasNext()) {
            Post post = i.next();
            if (post.getId().equals(postId)) {
                i.remove();
                break;
            }
        }
    }

    private List<UserPost> convert(List<Post> posts){
        User currentUser = userService.getLoggedInUser();
        List<User> users = userRepository.findAll();
        var postsList = new ArrayList<UserPost>();
        for (var user : users)
        {
            for (var post : user.getPosts())
            {
                if (posts != null && posts.stream().noneMatch(p -> p.getId().equals(post.getId())))
                {
                    continue;
                }
                var savesCount = users.stream().filter(u -> u.getSaved().stream().anyMatch(s -> s.getId().equals(post.getId()))).count();
                var userPost = new UserPost(user, post);

                var comments = new ArrayList<UserComment>();
                for (var comment : post.getComments())
                {
                    var commentAuthor = users.stream().filter(u -> u.getEmail().equals(comment.getUserEmail())).findFirst().orElse(null);
                    if (commentAuthor != null)
                    {
                        comments.add(new UserComment(comment, commentAuthor));
                    }
                }
                userPost.setComments(comments.stream().sorted((l, r) -> r.getLocalDateTime().compareTo(l.getLocalDateTime())).collect(Collectors.toList()));
                userPost.setLiked(userPost.getLikes().stream().anyMatch(like -> like.getUser().getId().equals(currentUser.getId())));
                userPost.setSaved(currentUser.getSaved().stream().anyMatch(p -> p.getId().equals(userPost.getId())));
                userPost.setRetweeted(currentUser.getRetweeted().stream().anyMatch(p -> p.getId().equals(userPost.getId())));
                userPost.setSavesCount(savesCount);
                postsList.add(userPost);
            }
        }
        return postsList.stream()
                .sorted((l, r) -> r.getCreatedTime().compareTo(l.getCreatedTime()))
                .collect(Collectors.toList());
    }
}
