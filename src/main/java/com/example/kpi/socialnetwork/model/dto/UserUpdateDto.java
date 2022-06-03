package com.example.kpi.socialnetwork.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class UserUpdateDto {
    @NotBlank(message = "Name is mandatory")
    private String fullName;
    private Long id;
    @Email(regexp = ".+@.+\\..+")
    @NotBlank(message = "Email is mandatory")
    private String email;
    private String background;
    private String avatar;
    private String phone;
    private String address;
    private String dateOfBirth;
}
