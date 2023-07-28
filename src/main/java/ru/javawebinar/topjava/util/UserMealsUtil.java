package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

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

        System.out.println(filteredByCustomCollector(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
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

        mealsWithExcess.sort(new Comparator<>() {
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
                .collect(toMap(UserMeal::getDate, UserMeal::getCalories, Integer::sum));

        return meals.stream()
                .filter(m -> TimeUtil.isBetweenHalfOpen(m.getTime(), startTime, endTime))
                .map(m -> mealWithExcess(m, caloriesByDate.get(m.getDate()) > caloriesPerDay))
                .sorted(comparing(UserMealWithExcess::getDateTime).reversed())
                .collect(toList());
    }

    private static UserMealWithExcess mealWithExcess(UserMeal meal, boolean excess) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    // Optional filter by custom collector
    public static List<UserMealWithExcess> filteredByCustomCollector(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // Helper class for storing calories and meals. Can be mapped to specific date
        class DateData {
            private int calories;

            private final List<UserMeal> meals;

            public DateData(int calories, List<UserMeal> meals) {
                this.calories = calories;
                this.meals = meals;
            }

            public void addCalories(int calories) {
                this.calories += calories;
            }

            public void addMeal(UserMeal meal) {
                meals.add(meal);
            }

            public void addMeals(List<UserMeal> meals) {
                this.meals.addAll(meals);
            }
        }

        return meals.stream().collect(
                // Accumulator is a HashMap<LocalDate, DateData>. DateData keeps meals in LinkedList
                new Collector<UserMeal, Map<LocalDate, DateData>, List<UserMealWithExcess>>() {
                    @Override
                    public Supplier<Map<LocalDate, DateData>> supplier() {
                        return HashMap::new;
                    }

                    @Override
                    public BiConsumer<Map<LocalDate, DateData>, UserMeal> accumulator() {
                        return (map, meal) -> map.compute(
                                meal.getDate(),
                                (date, dateData) -> {
                                    if (dateData == null) {
                                        dateData = new DateData(meal.getCalories(), new LinkedList<>());
                                    } else {
                                        dateData.addCalories(meal.getCalories());
                                    }

                                    if (TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                                        dateData.addMeal(meal);
                                    }
                                    return dateData;
                                });
                    }

                    @Override
                    public BinaryOperator<Map<LocalDate, DateData>> combiner() {
                        return (leftMap, rightMap) -> {
                            rightMap.forEach((date, dateData) -> leftMap.merge(
                                    date,
                                    dateData,
                                    (leftDateData, rightDateData) -> {
                                        leftDateData.addCalories(rightDateData.calories);
                                        leftDateData.addMeals(rightDateData.meals);
                                        return leftDateData;
                                    }
                            ));
                            return leftMap;
                        };
                    }

                    @Override
                    public Function<Map<LocalDate, DateData>, List<UserMealWithExcess>> finisher() {
                        return (map) -> {
                            List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();
                            map.forEach((date, dateData) -> {
                                boolean excess = dateData.calories > caloriesPerDay;
                                dateData.meals.forEach(
                                        m -> mealsWithExcess.add(mealWithExcess(m, excess))
                                );
                            });

                            mealsWithExcess.sort(comparing(UserMealWithExcess::getDateTime).reversed());
                            return mealsWithExcess;
                        };
                    }

                    @Override
                    public Set<Collector.Characteristics> characteristics() {
                        return EnumSet.of(Collector.Characteristics.UNORDERED);
                    }
                }
        );
    }
}
