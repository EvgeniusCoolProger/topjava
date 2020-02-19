package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll() {
        log.info("getAll from meals");
        List<Meal> meals = service.getAll(authUserId());
        return MealsUtil.getTos(meals, authUserCaloriesPerDay());
    }

    public List<MealTo> getAll(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("getAll from meals between dates and times");
        List<Meal> meals = service.getBetweenDates(startDate, endDate, authUserId());
        return MealsUtil.getFilteredTos(meals, authUserCaloriesPerDay(), startTime, endTime);
    }

    public Meal get(int id) {
        log.info("get meal {}", id);
        return service.get(id, authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create meal {}", meal);
        checkNew(meal);
        return service.create(meal, authUserId());
    }

    public void delete(int id) {
        log.info("delete meal {}", id);
        service.delete(id, authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update meal {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, authUserId());
    }

}