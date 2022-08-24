package com.example.kpi.socialnetwork.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(value = FriendshipException.class)
    public String friendshipHandler(FriendshipException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }

    @ExceptionHandler(value = LikeException.class)
    public String likeHandler(LikeException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }

    @ExceptionHandler(value = PostException.class)
    public String postHandler(PostException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }

}
