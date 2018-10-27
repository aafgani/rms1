package com.mitrais.rms.controller;

import com.mitrais.rms.dao.UserDao;
import com.mitrais.rms.dao.impl.UserDaoImpl;
import com.mitrais.rms.model.User;
import com.mitrais.rms.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/users/*")
public class UserServlet extends AbstractController
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        switch (req.getPathInfo()){
            case "/list" :
                listUser(req,resp);
                break;
            case "/add" :
                addUser(req,resp);
                break;
            case "/edit" :
                editUser(req,resp);
                break;
            case "/update" :
                updateUser(req,resp);
                break;
            case "/delete":
                deleteUser(req,resp);
                break;
                default:
                    listUser(req,resp);
        }
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        UserDao userDao = UserDaoImpl.getInstance();
        Optional<User> user = userDao.find(id);
        UserDao finalUserDao = userDao;
        user.ifPresent(u -> {
            finalUserDao.delete(u);});

        resp.sendRedirect(req.getContextPath()+req.getServletPath()+"/list");

    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        String userName = req.getParameter("username");
        String password = req.getParameter("userpass");
        UserDao userDao = UserDaoImpl.getInstance();
        userDao.update(new User(id,userName,password));

        listUser(req,resp);
    }

    private void addUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("GET".equalsIgnoreCase(req.getMethod())){
            String path = getTemplatePath(req.getServletPath()+"/form");
            RequestDispatcher requestDispatcher = req.getRequestDispatcher(path);
            requestDispatcher.forward(req, resp);
        }else{
            UserService userService = UserService.getInstance();
            Long id = userService.geUserbytLatestID().getId()+1;
            String userName = req.getParameter("username");
            String password = req.getParameter("userpass");
            UserDao userDao = UserDaoImpl.getInstance();
            userDao.save(new User(null,userName,password));

            listUser(req,resp);
        }
    }

    private void editUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDao userDao = UserDaoImpl.getInstance();
        Long id = Long.parseLong(req.getParameter("id"));
        Optional<User> user = userDao.find(id);
        user.ifPresent(u -> {
            req.setAttribute("user", u);
        });
        String path = getTemplatePath(req.getServletPath()+"/form");
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(path);
        requestDispatcher.forward(req, resp);
    }

    private void listUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDao userDao = UserDaoImpl.getInstance();
        List<User> users = userDao.findAll();
        req.setAttribute("users", users);

        String path = getTemplatePath(req.getServletPath()+"/list");
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(path);
        requestDispatcher.forward(req, resp);
    }
}
