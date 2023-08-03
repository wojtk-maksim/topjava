package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.ADMIN_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    private static int currentUserId = USER_ID;

    public static int authUserId() {
        return currentUserId;
    }

    public static void setAuthUserId(int userId) {
        SecurityUtil.currentUserId = userId;
    }

    public static int authUserCaloriesPerDay() {
        if (currentUserId == 2) {
            return ADMIN_CALORIES_PER_DAY;
        } else {
            return DEFAULT_CALORIES_PER_DAY;
        }
    }
}