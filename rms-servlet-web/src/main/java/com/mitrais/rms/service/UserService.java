package com.mitrais.rms.service;


import com.mitrais.rms.dao.UserDao;
import com.mitrais.rms.dao.impl.UserDaoImpl;
import com.mitrais.rms.model.User;

import java.util.Collections;
import java.util.List;

public class UserService {
    public static UserService userService;

    public static UserService getInstance(){
        if (userService==null){
            userService = new UserService();
        }

        return userService;
    }

    public User geUserbytLatestID(){

        UserDao userDao = UserDaoImpl.getInstance();
        List<User> users =  userDao.findAll();

        return users.get(users.size()-1);
    }
}
