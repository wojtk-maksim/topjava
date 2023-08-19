package ru.javawebinar.topjava.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.Meal;

@Component
public class MealValidator implements Validator {
    @Override
    public boolean supports(Class clazz) {
        return Meal.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Meal meal = (Meal) target;

        if (meal.getDateTime() == null) {
            errors.rejectValue("dateTime", "meal.dateTime.empty");
        }

        String description = meal.getDescription();
        if (description == null || description.isBlank()) {
            errors.rejectValue("description", "meal.description.empty");
        } else if (description.length() < 2 || description.length() > 120) {
            errors.rejectValue("description", "meal.description.invalid.length");
        }

        Integer calories = meal.getCalories();
        if (calories == null) {
            errors.rejectValue("calories", "meal.calories.empty");
        } else if (calories < 10 || calories > 5000) {
            errors.rejectValue("calories", "meal.calories.invalid.size");
        }
    }
}
