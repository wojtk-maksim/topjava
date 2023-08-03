package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import static ru.javawebinar.topjava.web.SecurityUtil.ADMIN_ID;
import static ru.javawebinar.topjava.web.SecurityUtil.USER_ID;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.userMeals.forEach(m -> save(m, USER_ID));
        MealsUtil.adminMeals.forEach(m -> save(m, ADMIN_ID));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {} for user {}", meal, userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        if (get(meal.getId(), userId) == null) {
            return null;
        }
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {} from user {}", id, userId);
        if (get(id, userId) == null) {
            return false;
        } else {
            return repository.remove(id) != null;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {} for user {}", id, userId);
        Meal meal = repository.get(id);
        return (meal == null || meal.getUserId() != userId) ? null : meal;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("getAll for user {}", userId);
        return filterByPredicate(userId, m -> true);
    }

    @Override
    public Collection<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getFiltered for user {} [startDate: {}, endDate: {}]", userId, startDate, endDate);
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

