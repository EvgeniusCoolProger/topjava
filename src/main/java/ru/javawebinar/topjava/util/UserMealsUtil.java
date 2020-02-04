package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410),
                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 1, 10, 0), "Завтрак", 400),
                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 1, 13, 0), "Обед", 400),
                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 1, 20, 0), "Ужин", 1100),
                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 2, 10, 0), "Завтрак", 909),
                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 2, 13, 0), "Обед", 1200),
                new UserMeal(LocalDateTime.of(2020, Month.MARCH, 2, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.MARCH, 2, 13, 0), "Обед", 1200)

        );
        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        List<UserMeal> emptyMeals = new ArrayList<>();
        List<UserMealWithExcess> mealsTo1 = filteredByCycles(emptyMeals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        System.out.println(mealsTo1);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        Map<LocalDate, Integer> datesAndSumCalories = new ConcurrentHashMap<>();
        if (!meals.isEmpty()) {
            for (UserMeal meal : meals) {
                datesAndSumCalories.merge(
                        meal.getDateTime().toLocalDate(),
                        meal.getCalories(),
                        (date, calories) -> calories + datesAndSumCalories.getOrDefault(meal.getDateTime().toLocalDate(), 0));
            }
            for (UserMeal meal : meals) {
                if ((TimeUtil.isBetweenInclusive(meal.getDateTime().toLocalTime(), startTime, endTime))) {
                    userMealWithExcessList.add(new UserMealWithExcess(
                            meal.getDateTime(),
                            meal.getDescription(),
                            meal.getCalories(),
                            datesAndSumCalories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
                }
            }
        } else {
            System.out.println("Нет еды");
        }
        return userMealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream().map(userMeal -> new UserMealWithExcess(
                userMeal.getDateTime(),
                userMeal.getDescription(),
                userMeal.getCalories(),
                meals.stream()
                        .filter(meal -> meal.getDateTime().toLocalDate().isEqual(userMeal.getDateTime().toLocalDate()))
                        .mapToInt(UserMeal::getCalories)
                        .sum() > caloriesPerDay)).
                filter(userMealWithExcess -> (TimeUtil.isBetweenInclusive(userMealWithExcess.getDateTime().toLocalTime(), startTime, endTime)))
                .collect(Collectors.toList());
    }
}
