package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals1.forEach(m -> save(m, 1));
        MealsUtil.meals2.forEach(m -> save(m, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        if (get(meal.getId(), userId) == null) {
            return null;
        }
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (get(id, userId) == null) {
            return false;
        } else {
            return repository.remove(id) != null;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        return meal.getUserId() == userId ? meal : null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return filterByPredicate(userId, m -> true);
    }

    @Override
    public Collection<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate == null ? null : LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime end = endDate == null ? null : LocalDateTime.of(endDate, LocalTime.MAX);
        return filterByPredicate(userId, m -> DateTimeUtil.isBetweenHalfOpen(m.getDateTime(), start, end));
    }

    private Collection<Meal> filterByPredicate(int userId, Predicate<Meal> dateFilter) {
        return repository.values().stream()
                .filter(dateFilter)
                .filter(m -> m.getUserId() == userId)
                .sorted(comparing(Meal::getDateTime).reversed())
                .collect(toList());
    }
}

