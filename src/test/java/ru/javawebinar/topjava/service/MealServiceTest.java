package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.testdata.MealTestData.*;
import static ru.javawebinar.topjava.testdata.TestUtil.assertMatch;
import static ru.javawebinar.topjava.testdata.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.testdata.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(USER_MEAL1_ID, USER_ID);
        assertMatch(meal, userMeal1);
    }

    @Test
    public void getNotYours() {
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_MEAL1_ID, USER_ID));
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotYours() {
        assertThrows(NotFoundException.class, () -> service.delete(ADMIN_MEAL1_ID, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> meals = service.getBetweenInclusive(START_DATE, END_DATE, USER_ID);
        assertMatch(filteredByDateMeals, meals);
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        assertMatch(meals, userMeals);
    }

    @Test
    public void update() {
        service.update(getUpdated(), USER_ID);
        assertMatch(service.get(USER_MEAL1_ID, USER_ID), getUpdated());
    }

    @Test
    public void updateNotYours() {
        assertThrows(NotFoundException.class, () -> service.update(getUpdated(), ADMIN_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Meal meal = getNew();
        meal.setId(created.getId());
        assertMatch(created, meal);
        assertMatch(service.get(meal.getId(), USER_ID), meal);
    }

    @Test
    public void createDuplicateDateTime() {
        assertThrows(DataAccessException.class, () -> service.create(getDuplicateDateTime(), USER_ID));
    }
}