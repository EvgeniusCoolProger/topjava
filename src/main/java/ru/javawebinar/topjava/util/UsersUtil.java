package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UsersUtil {

    public static final List<User> USERS = Arrays.asList(
            new User(null, "Петя", "petya@mail.ru", "133213", Role.ROLE_USER, Role.values()),
            new User(null, "Женя", "zhenya1985@mail.ru", "193132", Role.ROLE_USER, Role.values()),
            new User(null, "Антон", "anton1984@mail.ru", "231321", Role.ROLE_USER, Role.values()),
            new User(null, "Елена", "elena1996@mail.ru", "10983", Role.ROLE_USER, Role.values()),
            new User(null, "Катерина", "katherine1990@mail.ru", "129877", Role.ROLE_USER, Role.values()
    ));
}
