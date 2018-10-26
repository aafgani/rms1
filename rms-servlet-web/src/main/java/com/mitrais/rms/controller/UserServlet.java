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
        //String path = getTemplatePath(req.getServletPath()+req.getPathInfo());
        String servletPath = req.getServletPath();
        String pathInfo = req.getPathInfo();
        UserDao userDao = UserDaoImpl.getInstance();
        Long id;
        Optional<User> user;

        switch (req.getPathInfo()){
            case "/list" :
                userDao = UserDaoImpl.getInstance();
                List<User> users = userDao.findAll();
                req.setAttribute("users", users);
                break;
            case "/add" :
                if ("GET".equalsIgnoreCase(req.getMethod())){
                    pathInfo="/form";
                }
                else {
                    UserService userService = UserService.getInstance();
                    id = userService.geUserbytLatestID().getId()+1;
                    String userName = req.getParameter("username");
                    String password = req.getParameter("userpass");
                    userDao = UserDaoImpl.getInstance();
                    userDao.save(new User(null,userName,password));
                    pathInfo = "/list";
                    req.setAttribute("users", userDao.findAll());
                }
                break;
            case "/edit" :
                userDao = UserDaoImpl.getInstance();
                id = Long.parseLong(req.getParameter("id"));
                user = userDao.find(id);
                user.ifPresent(u -> {
                    req.setAttribute("user", u);
                });
                pathInfo = "/form";
                break;
            case "/update" :
                id = Long.parseLong(req.getParameter("id"));
                String userName = req.getParameter("username");
                String password = req.getParameter("userpass");
                userDao = UserDaoImpl.getInstance();
                userDao.update(new User(id,userName,password));
                pathInfo = "/list";
                req.setAttribute("users", userDao.findAll());
                break;
            case "/delete":
                id = Long.parseLong(req.getParameter("id"));
                userDao = UserDaoImpl.getInstance();
                user = userDao.find(id);
                UserDao finalUserDao = userDao;
                user.ifPresent(u -> {
                    finalUserDao.delete(u);});
                pathInfo="/list";
                req.setAttribute("users", userDao.findAll());
                break;
                default:
                    userDao = UserDaoImpl.getInstance();
                    users = userDao.findAll();
                    req.setAttribute("users", users);
        }

        RequestDispatcher requestDispatcher = req.getRequestDispatcher(getTemplatePath(servletPath+pathInfo));
        requestDispatcher.forward(req, resp);

        /*if ("/list".equalsIgnoreCase(req.getPathInfo())){
            UserDao userDao = UserDaoImpl.getInstance();
            List<User> users = userDao.findAll();
            req.setAttribute("users", users);
        }

        RequestDispatcher requestDispatcher = req.getRequestDispatcher(getTemplatePath(servletPath+pathInfo));
        requestDispatcher.forward(req, resp);*/
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        String userName = req.getParameter("username");
        String password = req.getParameter("userpass");
        UserDao userDao = UserDaoImpl.getInstance();
        userDao.update(new User(id,userName,password));
        listUser(req,resp);
    }

    private void addUser(HttpServletRequest req, HttpServletResponse resp) {
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
