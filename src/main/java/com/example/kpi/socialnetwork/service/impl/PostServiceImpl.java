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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of service methods for Post Entity
 * */
@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Saving post into DB
     * */
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

    /**
     * Creating post
     * @param content is the text user writes in the post
     * @param file is the picture which user attaches
     * @return UserPost is the dto object
     * */
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

        if (newPost.getImage() != null && !newPost.getImage().isEmpty()) {
            FileUploadUtil.saveFile(String.format("user-photos/posts/%d/", newPost.getId()), newPost.getImage(),file);
        }

        return new UserPost(user, newPost);
    }

    /**
     * Retrieving all posts of user
     * @param userId is the ID of user
     * */
    @Override
    public List<UserPost> getPostsOfUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return convert(user.getPosts());
    }

    @Override
    public List<UserPost> getAllPosts() {
        return convert(null);
    }

    /**
     * Retrieving post by ID
     * @param postId is the ID of post
     * */
    @Override
    public UserPost findById(Long postId) {
        return findById(postId, userService.getLoggedInUser());
    }

    /**
     * Retrieving post by ID and current logged in user
     * @param postId is the ID of post
     * @param currentUser is logged in user
     * */
    @Override
    public UserPost findById(Long postId, User currentUser) {
        var post = postRepository.findByIdFetchComments(postId).orElseThrow();
        var posts = new ArrayList<Post>();
        posts.add(post);
        var res = convert(posts, currentUser);
        return res.get(0);
    }

    /**
     * Retrieving saved posts of user
     * @param email is the email of user
     * */
    @Override
    public List<UserPost> getSavedPostsOfUser(String email) {
        User user = userRepository.findByEmail(email);
        return convert(user.getSaved());
    }

    /**
     * Retrieving liked posts of user
     * @param email is the email of user
     * */
    @Override
    public List<UserPost> getLikedPostsOfUser(String email) {
        User user = userRepository.findByEmail(email);
        return convert(user.getLikes());
    }

    /**
     * Removing of a post by ID
     * @param postId is the ID of post
     * */
    @Override
    public boolean deletePost(Long postId) {
        var user = userService.getLoggedInUser();
        return deletePost(postId, user);
    }

    /**
     * Removing of a post by ID
     * @param postId is the ID of post
     * @param email is the email of user
     * */
    @Override
    public boolean deletePost(Long postId, String email) {
        var user = userRepository.findByEmail(email);
        return deletePost(postId, user);
    }

    /**
     * Implementing delete functions using sql-query
     * */
    @Transactional
    @javax.transaction.Transactional
    private boolean deletePost(Long postId, User user) {
        var manager = entityManagerFactory.createEntityManager();
        var transaction = manager.getTransaction();
        try {
            transaction.begin();
            manager.createNativeQuery("DELETE FROM users_likes l where l.likes_id =:postId and l.user_id=:userId")
                    .setParameter("postId", postId)
                    .setParameter("userId", user.getId())
                    .executeUpdate();
            manager.createNativeQuery("DELETE FROM users_retweeted l where l.retweeted_id =:postId and l.user_id=:userId")
                    .setParameter("postId", postId)
                    .setParameter("userId", user.getId())
                    .executeUpdate();
            manager.createNativeQuery("DELETE FROM users_posts l where l.posts_id =:postId and l.user_id=:userId")
                    .setParameter("postId", postId)
                    .setParameter("userId", user.getId())
                    .executeUpdate();
            manager.createNativeQuery("DELETE FROM users_saved l where l.saved_id =:postId and l.user_id=:userId")
                    .setParameter("postId", postId)
                    .setParameter("userId", user.getId())
                    .executeUpdate();
            manager.flush();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        postRepository.deleteById(postId);
        return true;
    }

    /**
     * Editing of a post by ID
     * @param postId is the ID of a post
     * @param newContent is text of a post
     * @param file is an image user attaches
     * */
    @Override
    public Post editPost(long postId, String newContent, MultipartFile file) {
        var post = postRepository.findById(postId).orElse(null);if (post == null) {
            return null;
        }
        post.setContent(newContent);
        if (file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty() && !post.getImage().equals(file.getOriginalFilename())) {
            try {
                FileUploadUtil.saveFile(String.format("user-photos/posts/%d/", post.getId()), file.getOriginalFilename(),file);
            }
            catch (Exception ex) {
                return null;
            }
            post.setImage(file.getOriginalFilename());
        }
        return postRepository.save(post);
    }

    /**
     * Retweeting of a post
     * @param postId is the ID of a post
     * @param user is a current logged in user
     * */
    @Override
    public UserPost retweetPost(User user, Long postId) {
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
            if (newPost.getImage() != null && !newPost.getImage().isEmpty()) {
                try {
                    FileUploadUtil.copyFile(originalPost.getImagePath(), newPost.getImagePath());
                } catch (IOException ex) {
                    postRepository.deleteById(newPost.getId());
                    return null;
                }
            }

            user.getPosts().add(newPost);
            user.getRetweeted().add(originalPost);
            userRepository.save(user);
            return new UserPost(user, newPost);
        }
        return null;
    }

    private List<UserPost> convert(List<Post> posts){
        User currentUser = userService.getLoggedInUser();
        return convert(posts, currentUser);
    }

    /**
     * Converting object from dto to a db-model
     * */
    private List<UserPost> convert(List<Post> posts, User currentUser) {
        List<User> users = userRepository.findAll();
        var postsList = new ArrayList<UserPost>();
        for (var user : users) {
            for (var post : user.getPosts()) {
                if (posts != null && posts.stream().noneMatch(p -> p.getId().equals(post.getId()))) {
                    continue;
                }
                var savesCount = users.stream().filter(u -> u.getSaved().stream().anyMatch(s -> s.getId().equals(post.getId()))).count();
                var userPost = new UserPost(user, post);

                var comments = new ArrayList<UserComment>();
                for (var comment : post.getComments()) {
                    var commentAuthor = users.stream().filter(u -> u.getEmail().equals(comment.getUserEmail())).findFirst().orElse(null);
                    if (commentAuthor != null) {
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
