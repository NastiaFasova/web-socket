package com.example.kpi.socialnetwork.model.mapper;

import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.model.dto.UserLoginDto;
import org.springframework.stereotype.Component;

@Component
public class UserLoginMapper {

    public User getUser(UserLoginDto userLoginDto) {
        User user = new User();
        user.setEmail(userLoginDto.getEmail());
        user.setPassword(userLoginDto.getPassword());
        return user;
    }

    public UserLoginDto UserLogin(User user) {
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail(user.getEmail());
        userLoginDto.setPassword(user.getPassword());
        return userLoginDto;
    }
}
