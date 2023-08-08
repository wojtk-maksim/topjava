package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import java.util.Collections;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = "datajpa")
public class DatajpaUserServiceTest extends AbstractUserServiceTest {
    @Test
    public void getWithMeals() {
        User user = service.getWithMeals(USER_ID);
        USER_MATCHER.assertMatch(user, UserTestData.user);
        MEAL_MATCHER.assertMatch(user.getMeals(), meal1, meal2, meal3, meal4, meal5, meal6, meal7);
    }

    @Test
    public void getWithEmptyMeals() {
        User guest = service.getWithMeals(GUEST_ID);
        USER_MATCHER.assertMatch(guest, UserTestData.guest);
        MEAL_MATCHER.assertMatch(guest.getMeals(), Collections.emptyList());
    }
}
