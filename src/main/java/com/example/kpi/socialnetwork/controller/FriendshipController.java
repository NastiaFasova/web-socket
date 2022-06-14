package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.model.Friendship;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.service.FriendshipService;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class FriendshipController {
    private final UserService userService;
    private final FriendshipService friendshipService;

    @Autowired
    public FriendshipController(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    @GetMapping("/follow/{id}")
    public String follow(@PathVariable Long id) {
        User loggedInUser = userService.getLoggedInUser();
        if (id.equals(loggedInUser.getId())) {
            throw new RuntimeException();
        }
        friendshipService.follow(loggedInUser.getId(), id);
        return "redirect:/followings";
    }

    @PostMapping("/follow/{id}")
    public boolean followUser(@PathVariable Long id) {
        User loggedInUser = userService.getLoggedInUser();
        if (id.equals(loggedInUser.getId())) {
            return false;
        }
        return friendshipService.follow(loggedInUser.getId(), id) != null;
    }

    @GetMapping("/followings/{id}")
    public String myFollowings(Model model, @PathVariable Long id) throws NullPointerException {
        User user = userService.getUserById(id);
        List<User> followings = friendshipService.getFollowingsOfUser(user.getId());
        model.addAttribute("users", followings);
        model.addAttribute("modalId", "followingsId");
        model.addAttribute("modalTitle", String.format("%s is following", user.getFullName()));
        return "fragments/followers-modal :: followers-modal";
    }

    @GetMapping("/followers/{id}")
    public String myFollowers(Model model, @PathVariable Long id) throws NullPointerException {
        User user = userService.getUserById(id);
        List<User> followers = friendshipService.getFollowersOfUser(user.getId());
        model.addAttribute("users", followers);
        model.addAttribute("modalId", "followersId");
        model.addAttribute("modalTitle", String.format("%s are followed", user.getFullName()));
        return "fragments/followers-modal :: followers-modal";
    }
}
