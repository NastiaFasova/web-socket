package com.example.kpi.socialnetwork.model.mapper;

import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.model.dto.UserRegisterDto;
import org.springframework.stereotype.Component;

@Component
public class UserRegisterMapper {

    public User getUser(UserRegisterDto userRegisterDto) {
        User user = new User();
        user.setEmail(userRegisterDto.getEmail());
        user.setPassword(userRegisterDto.getPassword());
        user.setFullName(userRegisterDto.getFullName());
        user.setConfirmPassword(userRegisterDto.getConfirmPassword());
        user.setAvatar(userRegisterDto.getAvatar());
        user.setBackground(userRegisterDto.getBackground());
        user.setPhone(userRegisterDto.getPhone());
        user.setAddress(userRegisterDto.getAddress());
        user.setDateOfBirth(userRegisterDto.getDateOfBirth());
        return user;
    }

    public UserRegisterDto getUserRegister(User user) {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setEmail(user.getEmail());
        userRegisterDto.setPassword(user.getPassword());
        userRegisterDto.setFullName(user.getFullName());
        userRegisterDto.setConfirmPassword(user.getConfirmPassword());
        userRegisterDto.setAvatar(user.getAvatar());
        userRegisterDto.setBackground(user.getBackground());
        userRegisterDto.setPhone(user.getPhone());
        userRegisterDto.setAddress(user.getAddress());
        userRegisterDto.setDateOfBirth(user.getDateOfBirth());
        return userRegisterDto;
    }
}
