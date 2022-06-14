package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.common.UserFollow;
import com.example.kpi.socialnetwork.common.UserPost;
import com.example.kpi.socialnetwork.model.Friendship;
import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.model.dto.UserRegisterDto;
import com.example.kpi.socialnetwork.model.dto.UserUpdateDto;
import com.example.kpi.socialnetwork.model.mapper.UserUpdateMapper;
import com.example.kpi.socialnetwork.service.FriendshipService;
import com.example.kpi.socialnetwork.service.PostService;
import com.example.kpi.socialnetwork.service.UserService;
import com.example.kpi.socialnetwork.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final PostService postService;
    private final FriendshipService friendshipService;
    private final UserUpdateMapper userUpdateMapper;

    @Autowired
    public UserController(UserService userService, PostService postService,
                          FriendshipService friendshipService, UserUpdateMapper userUpdateMapper) {
        this.userService = userService;
        this.postService = postService;
        this.friendshipService = friendshipService;
        this.userUpdateMapper = userUpdateMapper;
    }

    @GetMapping("/me")
    public String myPosts(Model model) throws NullPointerException {
        User user = userService.getLoggedInUser();
        List<UserPost> posts = postService.getPostsOfUser(user.getId());
        List<UserFollow> followers = friendshipService.getFollowersOfUser(user.getId());
        List<UserFollow> followings = friendshipService.getFollowingsOfUser(user.getId());
        model.addAttribute("posts", posts);
        model.addAttribute("user", user);
        model.addAttribute("currentUser", user);
        model.addAttribute("followers", followers);
        model.addAttribute("followings", followings);
        model.addAttribute("isCurrentUser", true);
        return "profile";
    }

    @GetMapping("/user/{id}")
    public String userPosts(Model model, @PathVariable("id") long id) throws NullPointerException {
        User currentUser = userService.getLoggedInUser();
        if (currentUser.getId() == id)
        {
            return "redirect:/me";
        }
        User user = userService.getUserById(id);
        List<UserPost> posts = postService.getPostsOfUser(user.getId());
        List<UserFollow> followers = friendshipService.getFollowersOfUser(user.getId());
        List<UserFollow> followings = friendshipService.getFollowingsOfUser(user.getId());
        model.addAttribute("posts", posts);
        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("followers", followers);
        model.addAttribute("followings", followings);
        model.addAttribute("isCurrentUser", false);
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
    public String editProfileForm(Model model) {
        UserUpdateDto user = userUpdateMapper.getUserUpdateDto(userService.getLoggedInUser());
        model.addAttribute("formData", user);
        return "update";
    }

    @PostMapping("/edit")
    public String editProfile(@Valid @ModelAttribute("formData") UserUpdateDto userUpdateDto,
                              BindingResult bindingResult,
                              @RequestParam("fileImage") MultipartFile avatar) throws IOException {
        if (bindingResult != null && bindingResult.hasErrors()) {
            return "redirect:/edit";
        }
        if (avatar.getOriginalFilename() == null || avatar.getOriginalFilename().isEmpty()) {
            userService.save(userUpdateMapper.getUser(userUpdateDto));
            return "redirect:/me";
        }
        String fileName = StringUtils.cleanPath(avatar.getOriginalFilename());
        userUpdateDto.setAvatar(fileName);
        User registeredUser = userService.save(userUpdateMapper.getUser(userUpdateDto));
        String uploadDir = "user-photos/avatar/" + registeredUser.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, avatar);
        return "redirect:/me";
    }
}
