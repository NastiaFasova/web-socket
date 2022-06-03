package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.model.Friendship;
import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.model.dto.UserRegisterDto;
import com.example.kpi.socialnetwork.service.FriendshipService;
import com.example.kpi.socialnetwork.service.PostService;
import com.example.kpi.socialnetwork.service.UserService;
import com.example.kpi.socialnetwork.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final PostService postService;
    private final FriendshipService friendshipService;

    @Autowired
    public UserController(UserService userService, PostService postService,
                          FriendshipService friendshipService) {
        this.userService = userService;
        this.postService = postService;
        this.friendshipService = friendshipService;
    }

    @GetMapping("/me")
    public String myPosts(Model model) throws NullPointerException {
        User user = userService.getLoggedInUser();
        List<Post> posts = postService.getPostsOfUser(user.getId());
        List<Friendship> followers = friendshipService.getFollowersOfUser(user.getId());
        List<Friendship> followings = friendshipService.getFollowingsOfUser(user.getId());
        model.addAttribute("posts", posts);
        model.addAttribute("user", user);
        model.addAttribute("followers", followers);
        model.addAttribute("followings", followings);
        return "profile";
    }

    @PostMapping("/edit-photo")
    public String addBackground(@RequestParam("fileImage") MultipartFile avatar) throws IOException {
        if (avatar.getOriginalFilename() == null || avatar.getOriginalFilename().isEmpty()) {
            return "redirect:/";
        }
        User loggedInUser = userService.getLoggedInUser();
        String fileName = StringUtils.cleanPath(avatar.getOriginalFilename());
        loggedInUser.setAvatar(fileName);
        userService.save(loggedInUser);
        String uploadDir = "user-photos/" + loggedInUser.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, avatar);
        return "redirect:/me";
    }

    @GetMapping("/edit")
    public String editProfile(Model model) {
        User user = userService.getLoggedInUser();
        model.addAttribute("formData", user);
        return "update";
    }
}
