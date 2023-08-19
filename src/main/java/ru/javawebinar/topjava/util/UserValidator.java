package ru.javawebinar.topjava.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.HasUserAttributes;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator {
    private final Pattern emailMatcher = Pattern.compile("^(.+)@(\\S+)$");

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean supports(Class clazz) {
        return HasUserAttributes.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasUserAttributes user = (HasUserAttributes) target;

        if (user.getName() == null || user.getName().isBlank()) {
            errors.rejectValue("name", "user.name.empty");
        } else if (user.getName().length() < 2 || user.getName().length() > 100) {
            errors.rejectValue("name", "user.name.invalid.length");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            errors.rejectValue("email", "user.email.empty");
        } else if (user.getEmail().length() > 100) {
            errors.rejectValue("email", "user.email.invalid.length");
        } else if (!emailMatcher.matcher(user.getEmail()).matches()) {
            errors.rejectValue("email", "user.email.invalid.format");
        } else if (userRepository.getByEmail(user.getEmail()) != null) {
            errors.rejectValue("email", "user.email.already.exists");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            errors.rejectValue("password", "user.password.empty");
        } else if (user.getPassword().length() < 5 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "user.password.invalid.length");
        }

        if (user.getCaloriesPerDay() == null) {
            errors.rejectValue("caloriesPerDay", "user.calories.empty");
        } else if (user.getCaloriesPerDay() < 10 || user.getCaloriesPerDay() > 10000) {
            errors.rejectValue("caloriesPerDay", "user.calories.invalid.size");
        }
    }
}
