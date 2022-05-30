package com.example.kpi.socialnetwork.model.dto;

import com.example.kpi.socialnetwork.validation.PasswordMatch;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@PasswordMatch
public class UserRegisterDto {
    private String fullName;
    @Email(regexp = ".+@.+\\..+")
    private String email;
    private String password;
    private String confirmPassword;
    private String image;
}
