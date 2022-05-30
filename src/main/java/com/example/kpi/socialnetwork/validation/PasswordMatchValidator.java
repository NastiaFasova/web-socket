package com.example.kpi.socialnetwork.validation;

import com.example.kpi.socialnetwork.model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements
        ConstraintValidator<PasswordMatch, User> {

    @Override
    public boolean isValid(User user,
                           ConstraintValidatorContext constraintValidatorContext) {
        String password = user.getPassword();
        String repeatPassword = user.getConfirmPassword();
        return password != null && password.equals(repeatPassword);
    }
}
