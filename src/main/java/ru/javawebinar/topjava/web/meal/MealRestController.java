package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll() {
        int userId = authUserId();
        log.info("getAll for user {}", userId);
        return service.getAll(authUserId(), authUserCaloriesPerDay());
    }

    public List<MealTo> getFiltered(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        int userId = authUserId();
        log.info("getFiltered for user {} [startDate: {}, startIme: {}, endDate: {}, endTime: {}]", userId, startDate, endDate, startTime, endTime);
        return service.getFiltered(authUserId(), authUserCaloriesPerDay(), startDate, endDate, startTime, endTime);
    }

    public Meal get(int id) {
        int userId = authUserId();
        log.info("get {} for  user {}", id, userId);
        return service.get(id, userId);
    }

    public Meal create(Meal meal) {
        int userId = authUserId();
        log.info("create for user {}", userId);
        checkNew(meal);
        return service.create(meal, userId);
    }

    public void delete(int id) {
        int userId = authUserId();
        log.info("delete {} from user {}", id, userId);
        service.delete(id, userId);
    }

    public void update(Meal meal, int id) {
        int userId = authUserId();
        log.info("update {} for user {}", meal, userId);
        service.update(meal, userId);
    }
}