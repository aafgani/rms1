package com.mitrais.rms.service;

import com.mitrais.rms.dao.UserDao;
import com.mitrais.rms.dao.impl.UserDaoImpl;
import com.mitrais.rms.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @org.junit.jupiter.api.Test
    void geUserbytLatestID() {
        UserService userService = UserService.getInstance();
        UserDao userDao = UserDaoImpl.getInstance();

        User user = new User();
        userDao.save(user);

//        assertEquals(user, user);
    }
}