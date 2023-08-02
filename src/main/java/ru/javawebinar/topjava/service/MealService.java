package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public List<MealTo> getAll(int userId, int caloriesPerDay) {
        return MealsUtil.getTos(repository.getAll(userId), caloriesPerDay);
    }

    public List<MealTo> getFiltered(int userId, int caloriesPerDay, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return MealsUtil.getFilteredTos(repository.getFiltered(userId, startDate, endDate), caloriesPerDay, startTime, endTime);
    }

    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    public Meal update(Meal meal, int userId) {
        return checkNotFoundWithId(repository.save(meal, userId), userId);
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }
}