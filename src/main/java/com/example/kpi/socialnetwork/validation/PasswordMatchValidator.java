package com.example.kpi.socialnetwork.validation;

import com.example.kpi.socialnetwork.model.dto.UserRegisterDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator of password and repeated password fields
 * */
public class PasswordMatchValidator implements
        ConstraintValidator<PasswordMatch, UserRegisterDto> {

    /**
     * Checks whether password and repeated password fields match
     * */
    @Override
    public boolean isValid(UserRegisterDto user,
                           ConstraintValidatorContext constraintValidatorContext) {
        String password = user.getPassword();
        String repeatPassword = user.getConfirmPassword();
        return password != null && password.equals(repeatPassword);
    }
}
