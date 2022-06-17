package com.example.kpi.socialnetwork.model.mapper;

import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.model.dto.UserUpdateDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class UserUpdateMapper {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public User getUser(UserUpdateDto userUpdateDto, User loggedInUser) {
        User user = new User();
        user.setEmail(userUpdateDto.getEmail());
        user.setId(userUpdateDto.getId());
        user.setFullName(userUpdateDto.getFullName());
        user.setAvatar(userUpdateDto.getAvatar());
        user.setBackground(userUpdateDto.getBackground());
        user.setPhone(userUpdateDto.getPhone());
        user.setAddress(userUpdateDto.getAddress());
        if (userUpdateDto.getDateOfBirth() != null && !userUpdateDto.getDateOfBirth().isEmpty())
        {
            user.setDateOfBirth(LocalDate.parse(userUpdateDto.getDateOfBirth(), formatter));
        }
        user.setPosts(loggedInUser.getPosts());
        user.setSaved(loggedInUser.getSaved());
        user.setLikes(loggedInUser.getLikes());
        user.setRetweeted(loggedInUser.getRetweeted());
        return user;
    }

    public UserUpdateDto getUserUpdateDto(User loggedInUser) {
        UserUpdateDto user = new UserUpdateDto();
        user.setEmail(loggedInUser.getEmail());
        user.setId(loggedInUser.getId());
        user.setFullName(loggedInUser.getFullName());
        user.setAvatar(loggedInUser.getAvatar());
        user.setBackground(loggedInUser.getBackground());
        user.setPhone(loggedInUser.getPhone());
        user.setAddress(loggedInUser.getAddress());
        return user;
    }
}
