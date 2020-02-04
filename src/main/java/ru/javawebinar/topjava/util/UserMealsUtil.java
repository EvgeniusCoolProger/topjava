package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 1, 10, 0), "Завтрак", 700),
                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 1, 13, 0), "Обед", 900),
                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 1, 20, 0), "Ужин", 1100),
                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 2, 10, 0), "Завтрак", 909),
                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 2, 13, 0), "Обед", 1200)
        );
Date date = new Date();
        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        Date date1 = new Date();
        System.out.println(date1.getTime() - date.getTime());
        mealsTo.forEach(System.out::println);
        Date date2 = new Date();
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        Date date3 = new Date();
        System.out.println(date3.getTime() - date2.getTime());
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        if (!meals.isEmpty()) {
            for (int i = 0; i < meals.size(); i++) {
                int finalI = i;
                userMealWithExcessList.add(new UserMealWithExcess(
                        meals.get(i).getDateTime(),
                        meals.get(i).getDescription(),
                        meals.get(i).getCalories(),
                        meals.stream().filter(meal -> meal.getDateTime().toLocalDate()
                                .isEqual(meals.get(finalI).getDateTime().toLocalDate()))
                                .mapToInt(UserMeal::getCalories).sum() > caloriesPerDay)
                );
            }
            userMealWithExcessList.removeIf(x -> !(TimeUtil.isBetweenInclusive(x.getDateTime().toLocalTime(), startTime, endTime)));
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
