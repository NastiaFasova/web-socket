package com.example.kpi.socialnetwork.model.Responses;

import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.model.dto.EditUserDto;
import lombok.Data;

@Data
public class UserResponse {
    private Long userId;
    private String fullName;
    private String description;
    private String avatar;
    private String panorama;

    public UserResponse(EditUserDto userDto) {
        userId = userDto.getId();
        fullName = userDto.getFullName();
        description = userDto.getDesc();
        if (userDto.getAvatar() != null && !userDto.getAvatar().isEmpty())
        {
            avatar = String.format("/user-photos/avatar/%d/%s", userId, userDto.getAvatar());
        }
        if (userDto.getPanorama() != null && !userDto.getPanorama().isEmpty())
        {
            panorama = String.format("/user-photos/panorama/%d/%s", userId, userDto.getPanorama());
        }
    }

    public UserResponse(User userDto) {
        userId = userDto.getId();
        fullName = userDto.getFullName();
        description = userDto.getDescription();
        if (userDto.getAvatar() != null && !userDto.getAvatar().isEmpty())
        {
            avatar = String.format("/user-photos/avatar/%d/%s", userId, userDto.getAvatar());
        }
        if (userDto.getBackground() != null && !userDto.getBackground().isEmpty())
        {
            panorama = String.format("/user-photos/panorama/%d/%s", userId, userDto.getBackground());
        }
    }
}
