package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    // Old style Java
    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDate = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate date = meal.getDate();
            Integer calories = caloriesByDate.get(date);
            if (calories == null) {
                caloriesByDate.put(date, meal.getCalories());
            } else {
                caloriesByDate.put(date, calories + meal.getCalories());
            }
        }

        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                mealsWithExcess.add(
                        mealWithExcess(meal, caloriesByDate.get(meal.getDate()) > caloriesPerDay)
                );
            }
        }

        mealsWithExcess.sort(new Comparator<UserMealWithExcess>() {
            @Override
            public int compare(UserMealWithExcess o1, UserMealWithExcess o2) {
                return o2.getDateTime().compareTo(o1.getDateTime());
            }
        });

        return mealsWithExcess;
    }

    // New style Java
    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDate = meals.stream()
                .collect(Collectors.toMap(UserMeal::getDate, UserMeal::getCalories, Integer::sum));

        return meals.stream()
                .filter(m -> TimeUtil.isBetweenHalfOpen(m.getTime(), startTime, endTime))
                .map(m -> mealWithExcess(m, caloriesByDate.get(m.getDate()) > caloriesPerDay))
                .sorted(Comparator.comparing(UserMealWithExcess::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private static UserMealWithExcess mealWithExcess(UserMeal meal, boolean excess) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
