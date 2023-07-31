package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoInMemory implements MealDao {
    private static final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    private static final AtomicInteger idCounter = new AtomicInteger(0);

    static {
        Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        ).forEach(m -> {
            int id = idCounter.incrementAndGet();
            meals.put(id, new Meal(id, m));
        });
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal get(int id) {
        return meals.get(id);
    }

    @Override
    public void save(Meal meal) {
        Integer id = meal.getId();
        if (id == null) {
            id = idCounter.incrementAndGet();
            Meal newMeal = new Meal(id, meal);
            meals.compute(id, (k, v) -> newMeal);
        } else {
            meals.computeIfPresent(id, (k, v) -> meal);
        }
    }

    @Override
    public void delete(int id) {
        meals.remove(id);
    }
}
