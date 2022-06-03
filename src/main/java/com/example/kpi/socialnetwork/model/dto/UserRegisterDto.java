package com.example.kpi.socialnetwork.model.dto;

import com.example.kpi.socialnetwork.validation.PasswordMatch;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@PasswordMatch()
public class UserRegisterDto {
    @NotBlank(message = "Name is mandatory")
    private String fullName;
    @Email(regexp = ".+@.+\\..+")
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 6)
    private String password;
    @Size(min = 6)
    private String confirmPassword;
    private String background;
    private String avatar;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
}
