package ru.javawebinar.topjava.web;

import java.util.concurrent.atomic.AtomicInteger;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {

    private static AtomicInteger count = new AtomicInteger(0);

    public static int authUserId() {
        return count.incrementAndGet();
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}