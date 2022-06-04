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

    @GetMapping("/followings")
    public String myFollowings(Model model) throws NullPointerException {
        User user = userService.getLoggedInUser();
        List<User> followings = friendshipService.getFollowingsOfUser(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("users", followings);
        return "users_list";
    }

    @GetMapping("/followers")
    public String myFollowers(Model model) throws NullPointerException {
        User user = userService.getLoggedInUser();
        List<User> followers = friendshipService.getFollowersOfUser(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("users", followers);
        return "users_list";
    }
}
