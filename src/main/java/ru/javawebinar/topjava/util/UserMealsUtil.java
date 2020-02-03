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
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        meals.sort(Comparator.comparing(UserMeal::getDateTime));
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        List<Integer> trueOrFalse = new ArrayList<>();
        LocalDate currentDate = meals.get(0).getDateTime().toLocalDate();
        int caloriesOfThisDay = 0;
        int count = 0;
        boolean isCurrentDateChanged = false;

        for (int i = 0; i < meals.size(); i++) {
            if (isCurrentDateChanged) {
                i--;
            }
            if (i == meals.size() - 1) {
                caloriesOfThisDay += meals.get(i).getCalories();
                count++;
                for (int j = 0; j < count; j++) {
                    trueOrFalse.add(1);
                }
            } else {
                if (currentDate.equals(meals.get(i).getDateTime().toLocalDate())) {
                    caloriesOfThisDay += meals.get(i).getCalories();
                    count++;
                    isCurrentDateChanged = false;
                } else {
                    if (caloriesOfThisDay > caloriesPerDay) {
                        for (int j = 0; j < count; j++) {
                            trueOrFalse.add(1);
                        }
                    } else {
                        for (int j = 0; j < count; j++) {
                            trueOrFalse.add(0);
                        }
                    }
                    count = 0;
                    caloriesOfThisDay = 0;
                    currentDate = meals.get(i).getDateTime().toLocalDate();
                    isCurrentDateChanged = true;
                }
            }
        }
        for (int i = 0; i < meals.size(); i++) {
            userMealWithExcessList.add(new UserMealWithExcess(meals.get(i).getDateTime(), meals.get(i).getDescription(),
                    meals.get(i).getCalories(), trueOrFalse.get(i) == 1));
        }
        userMealWithExcessList.removeIf(x -> !(x.getDateTime().toLocalTime().isAfter(startTime) && x.getDateTime().toLocalTime().isBefore(endTime)));
        return userMealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream().map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),
                meals.stream().filter(meal -> meal.getDateTime().toLocalDate().isEqual(userMeal.getDateTime().toLocalDate())).mapToInt(UserMeal::getCalories).sum() > caloriesPerDay)).
                filter(userMealWithExcess -> (userMealWithExcess.getDateTime().toLocalTime().isAfter(startTime)) && (userMealWithExcess.getDateTime().toLocalTime().isBefore(endTime))).
                collect(Collectors.toList());
    }
}
