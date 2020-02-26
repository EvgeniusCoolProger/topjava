package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void getAll() {
        List<Meal> meals = getList();
        List<Meal> actual = service.getAll(USER_ID);
        Assert.assertEquals(meals, actual);
    }

    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID, USER_ID);
        assertThat(meal).isEqualTo(MEAL);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(4, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void delete() {
        service.delete(MEAL_ID, USER_ID);
        service.get(MEAL_ID, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(MEAL_ID, ADMIN_ID);
    }

    @Test
    public void getBetweenHalfOpen() {
        List<Meal> expected = getList31thJanuary();
        List<Meal> expected1 = getListWithoutStartDate();
        List<Meal> actual = service.getBetweenHalfOpen(
                LocalDate.of(2020, Month.JANUARY, 31),
                LocalDate.of(2020, Month.JANUARY, 31),
                USER_ID);
        List<Meal> actual1 = service.getBetweenHalfOpen(null,
                LocalDate.of(2020, Month.JANUARY, 30),
                USER_ID);
        List<Meal> actual2 = service.getBetweenHalfOpen(
                LocalDate.of(2020, Month.JANUARY, 31),
                null,
                USER_ID);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected1, actual1);
        Assert.assertEquals(expected, actual2);
    }

    @Test
    public void update() {
        Meal updated = MealTestData.getUpdated();
        service.update(updated, USER_ID);
        assertThat(updated).isEqualTo(service.get(MEAL_ID, USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        Meal updated = getUpdated();
        service.update(updated, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertThat(created).isEqualTo(newMeal);
        assertThat(service.get(newId, USER_ID)).isEqualTo(newMeal);
    }
}
