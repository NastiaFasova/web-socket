package com.example.kpi.socialnetwork.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditUserDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String fullName;
    @JsonProperty("desc")
    private String desc;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("panorama")
    private String panorama;

    public EditUserDto(UserUpdateDto userDto) {
        id = userDto.getId();
        fullName = userDto.getFullName();
        desc = userDto.getDescription();
        avatar = userDto.getAvatar();
        panorama = userDto.getBackground();
    }
}
