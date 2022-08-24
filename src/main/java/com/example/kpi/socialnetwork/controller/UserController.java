package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.common.UserFollow;
import com.example.kpi.socialnetwork.common.UserPost;
import com.example.kpi.socialnetwork.model.Responses.UserResponse;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.model.dto.EditUserDto;
import com.example.kpi.socialnetwork.model.dto.UserUpdateDto;
import com.example.kpi.socialnetwork.model.mapper.UserUpdateMapper;
import com.example.kpi.socialnetwork.service.FriendshipService;
import com.example.kpi.socialnetwork.service.PostService;
import com.example.kpi.socialnetwork.service.UserService;
import com.example.kpi.socialnetwork.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * A controller class for a User Entity
 * */
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PostService postService;
    private final FriendshipService friendshipService;
    private final UserUpdateMapper userUpdateMapper;
    private final SimpMessagingTemplate messageTemplate;

    /**
     * Retrieving and displaying all posts of a logged in user
     * */
    @GetMapping("/me")
    public String myPosts(Model model) throws NullPointerException {
        User user = userService.getLoggedInUser();
        List<UserPost> posts = postService.getPostsOfUser(user.getId());
        List<UserFollow> followers = friendshipService.getFollowersOfUser(user.getId());
        List<UserFollow> followings = friendshipService.getFollowingsOfUser(user.getId());
        model.addAttribute("posts", posts);
        model.addAttribute("user", new UserFollow(user, followings.stream().anyMatch(u -> u.getUser().getId().equals(user.getId())), followings.size()));
        model.addAttribute("currentUser", user);
        model.addAttribute("followers", followers);
        model.addAttribute("followings", followings);
        model.addAttribute("isCurrentUser", true);
        return "profile";
    }

    /**
     * Retrieving and displaying all posts of a chosen user
     * */
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
        model.addAttribute("user", new UserFollow(user, followings.stream().anyMatch(u -> u.getUser().getId().equals(user.getId())), followings.size()));
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("followers", followers);
        model.addAttribute("followings", followings);
        model.addAttribute("isCurrentUser", false);
        return "profile";
    }

    /**
     * Edit user photos
     * */
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

    /**
     * Display a form for editing user profile
     * */
    @GetMapping("/edit")
    public String editProfileForm(Model model) {
        UserUpdateDto user = userUpdateMapper.getUserUpdateDto(userService.getLoggedInUser());
        model.addAttribute("formData", user);
        return "update";
    }

    /**
     * Edit a user profile and save changes into DB
     * */
    @PostMapping("/edit")
    public String editProfile(@Valid @ModelAttribute("formData") UserUpdateDto userUpdateDto,
                              BindingResult bindingResult,
                              @RequestParam("user-avatar") MultipartFile avatar,
                              @RequestParam("bg") MultipartFile panorama) throws IOException {
        if (bindingResult != null && bindingResult.hasErrors()) {
            return "redirect:/edit";
        }
        var user = userService.getLoggedInUser();
        if (avatar.getOriginalFilename() != null && !avatar.getOriginalFilename().isEmpty()) {
            String avatarFileName = StringUtils.cleanPath(avatar.getOriginalFilename());
            userUpdateDto.setAvatar(avatarFileName);
            FileUploadUtil.saveFile("user-photos/avatar/" + user.getId(), avatarFileName, avatar);
        } else {
            userUpdateDto.setAvatar(user.getAvatar());
        }
        if (panorama.getOriginalFilename() != null && !panorama.getOriginalFilename().isEmpty()) {
            String panoramaFileName = StringUtils.cleanPath(panorama.getOriginalFilename());
            userUpdateDto.setBackground(panoramaFileName);
            FileUploadUtil.saveFile("user-photos/panorama/" + user.getId(), panoramaFileName, panorama);
        } else {
            userUpdateDto.setBackground(user.getBackground());
        }
        User registeredUser = userService.save(userUpdateMapper.getUser(userUpdateDto, user));

        messageTemplate.convertAndSend("/topic/users/edit", new UserResponse(registeredUser));
        return "redirect:/me";
    }

    /**
     * Implementing endpoint for instant displaying of user profile changes
     * */
    @MessageMapping("/users/edit")
    @SendTo({"/topic/users/edit"})
    public UserResponse sendUser(EditUserDto userDto){
        return new UserResponse(userDto);
    }
}
