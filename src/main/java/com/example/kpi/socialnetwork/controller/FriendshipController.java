package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.common.UserFollow;
import com.example.kpi.socialnetwork.exceptions.FriendshipException;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.service.FriendshipService;
import com.example.kpi.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * A controller class for a Friendship entity
 * */
@Controller
@RequiredArgsConstructor
public class FriendshipController {
    private final UserService userService;
    private final FriendshipService friendshipService;

    /**
     * Displaying a button 'Follow'
     * @param id is an ID of user you want to follow
     * */
    @GetMapping("/follow/{id}")
    public String follow(@PathVariable Long id) {
        User loggedInUser = userService.getLoggedInUser();
        if (id.equals(loggedInUser.getId())) {
            throw new FriendshipException("User cannot follow himself");
        }
        friendshipService.follow(loggedInUser.getId(), id);
        return "redirect:/followings";
    }

    /**
     * Following a user
     * @param id is an ID of user you want to follow
     * */
    @PostMapping("/follow/{id}")
    public boolean followUser(@PathVariable Long id) {
        User loggedInUser = userService.getLoggedInUser();
        if (id.equals(loggedInUser.getId())) {
            return false;
        }
        return friendshipService.follow(loggedInUser.getId(), id) != null;
    }


    /**
     * Displaying followings of a user
     * @param id is an ID of a current logged in user
     * */
    @GetMapping("/followings/{id}")
    public String myFollowings(Model model, @PathVariable Long id) throws NullPointerException {
        User user = userService.getUserById(id);
        List<UserFollow> followings = friendshipService.getFollowingsOfUser(user.getId());
        model.addAttribute("users", followings);
        model.addAttribute("modalId", "followingsId");
        model.addAttribute("modalTitle", String.format("%s is following", user.getFullName()));
        return "fragments/followers-modal :: followers-modal";
    }

    /**
     * Displaying followers of a user
     * @param id is an ID of a current logged in user
     * */
    @GetMapping("/followers/{id}")
    public String myFollowers(Model model, @PathVariable Long id) throws NullPointerException {
        User user = userService.getUserById(id);
        List<UserFollow> followers = friendshipService.getFollowersOfUser(user.getId());
        model.addAttribute("users", followers);
        model.addAttribute("modalId", "followersId");
        model.addAttribute("modalTitle", String.format("%s are followed", user.getFullName()));
        return "fragments/followers-modal :: followers-modal";
    }
}
