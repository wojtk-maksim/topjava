package ru.javawebinar.topjava.testdata;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.testdata.UserTestData.GUEST_ID;

public class MealTestData {
    public static final int USER_MEAL1_ID = GUEST_ID + 1;
    public static final int USER_MEAL2_ID = USER_MEAL1_ID + 1;
    public static final int USER_MEAL3_ID = USER_MEAL2_ID + 1;
    public static final int USER_MEAL4_ID = USER_MEAL3_ID + 1;
    public static final int USER_MEAL5_ID = USER_MEAL4_ID + 1;
    public static final int USER_MEAL6_ID = USER_MEAL5_ID + 1;
    public static final int USER_MEAL7_ID = USER_MEAL6_ID + 1;
    public static final int ADMIN_MEAL1_ID = USER_MEAL7_ID + 1;
    public static final int ADMIN_MEAL2_ID = ADMIN_MEAL1_ID + 1;

    public static final Meal userMeal1 = new Meal(USER_MEAL1_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal userMeal2 = new Meal(USER_MEAL2_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal userMeal3 = new Meal(USER_MEAL3_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal userMeal4 = new Meal(USER_MEAL4_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal userMeal5 = new Meal(USER_MEAL5_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal userMeal6 = new Meal(USER_MEAL6_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal userMeal7 = new Meal(USER_MEAL7_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
    public static final Meal adminMeal1 = new Meal(ADMIN_MEAL1_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Админ обед", 700);
    public static final Meal adminMeal2 = new Meal(ADMIN_MEAL2_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 18, 0), "Админ ужин", 800);

    public static final List<Meal> userMeals = Arrays.asList(userMeal7, userMeal6, userMeal5, userMeal4, userMeal3, userMeal2, userMeal1);

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2020, Month.JANUARY, 25, 13, 0), "Новая еда", 1000);
    }

    public static Meal getUpdated() {
        return new Meal(USER_MEAL1_ID, LocalDateTime.of(2020, Month.JANUARY, 28, 20, 0), "Обновленная еда", 350);
    }

    public static Meal getDuplicateDateTime() {
        return new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    }

    public static final LocalDate START_DATE = LocalDate.of(2020, 1, 28);
    public static final LocalDate END_DATE = LocalDate.of(2020, 1, 30);
    public static final List<Meal> filteredByDateMeals = Arrays.asList(userMeal3, userMeal2, userMeal1);
}
